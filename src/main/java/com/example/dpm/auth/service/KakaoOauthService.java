package com.example.dpm.auth.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.dpm.auth.dto.KakaoInfoDto;
import com.example.dpm.member.dto.MemberDto;
import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class KakaoOauthService {
	private final MemberService memberService;

	// 카카오 API 호출해서 AccessToken으로 유저정보 가져오기
	public Map<String, Object> getUserAttributesByToken(String accessToken) {
		return WebClient.create().get().uri("https://kapi.kakao.com/v2/user/me")
				.headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken)).retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
				}).block();
	}

	// 카카오 API에서 가져온 유저정보를 DB에 저장
	public MemberDto getUserProfileByToken(String accessToken, String refreshToken) {
		Map<String, Object> userAttributesByToken = getUserAttributesByToken(accessToken);

		KakaoInfoDto kakaoInfoDto = new KakaoInfoDto(userAttributesByToken);

		MemberDto memberDto = MemberDto.builder().member_id(kakaoInfoDto.getId()).socialId(kakaoInfoDto.getEmail())
				.nickname(kakaoInfoDto.getNickname()).profile_image(kakaoInfoDto.getProfileImage()).point(0)
				.attendance(false).is_deleted(false).refreshToken(refreshToken).build();

		MemberEntity memberEntity = memberService.toEntity(memberDto);
		memberService.save(memberEntity);

		return memberDto;
	}
}