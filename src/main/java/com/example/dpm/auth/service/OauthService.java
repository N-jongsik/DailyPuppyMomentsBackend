package com.example.dpm.auth.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.dpm.auth.dto.TokenResponseDto;
import com.example.dpm.exception.CustomException;
import com.example.dpm.exception.ErrorCode;
import com.example.dpm.member.dto.MemberDto;
import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.member.service.MemberService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OauthService {
	private final MemberService memberService;
	private final JwtTokenService jwtTokenService;
	private final KakaoOauthService kakaoOauthService;

	public Map<String, Object> getKakaoToken(String code) {
		WebClient webClient = WebClient.builder().baseUrl("https://kauth.kakao.com")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).build();

		// 토큰 요청 파라미터 설정
		return webClient.post().uri("/oauth/token")
				.body(BodyInserters.fromFormData("grant_type", "authorization_code")
						.with("client_id", "d8fabac493f22b719a1bc4f29b44c9d1")
						.with("redirect_uri", "http://localhost:8080/login/oauth/kakao").with("code", code))
				.retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
				}).block();
	}

	public Map<String, Object> refreshAccessToken(String refreshToken) {
		WebClient webClient = WebClient.builder().baseUrl("https://kauth.kakao.com")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).build();

		// Refresh Token을 이용한 Access Token 갱신 요청
		Map<String, Object> tokenResponse = webClient.post().uri("/oauth/token")
				.body(BodyInserters.fromFormData("grant_type", "refresh_token")
						.with("client_id", "d8fabac493f22b719a1bc4f29b44c9d1").with("refresh_token", refreshToken))
				.retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
				}).block();

		return tokenResponse; // 새로운 Access Token과 Refresh Token을 포함한 응답
	}

	public Map<String, Object> logout(String accessToken) {
		WebClient webClient = WebClient.builder().baseUrl("https://kapi.kakao.com")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Access Token을 Authorization 헤더에 추가
				.build();

		// 로그아웃 요청
		Map<String, Object> logoutResponse = webClient.post().uri("/v1/user/logout").retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
				}).block();
		return logoutResponse; // 로그아웃 결과 응답
	}

	public String loginWithKakao(String accessToken, String refreshToken, HttpServletResponse response) {
		MemberDto memberDto = kakaoOauthService.getUserProfileByToken(accessToken, refreshToken);

		if (memberDto == null) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);
		}
		// 자체적인 JWT 토큰 생성
		String JWTToken = jwtTokenService.createJWTToken(accessToken, refreshToken,
				String.valueOf(memberDto.getMember_id()));

		// TokenResponseDto 생성
		TokenResponseDto tokenResponseDto = new TokenResponseDto();
		tokenResponseDto.setAccessToken(accessToken);
		tokenResponseDto.setRefreshToken(refreshToken);
		return JWTToken; // TokenResponseDto 객체 반환
	}

	public MemberDto UserInfo(String accessToken, String refreshToken, HttpServletResponse response) {
		MemberDto memberDto = kakaoOauthService.getUserProfileByToken(accessToken, refreshToken);
		return memberDto;
	}
}