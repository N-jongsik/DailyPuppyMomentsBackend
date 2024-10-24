package com.example.dpm.auth.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dpm.auth.dto.OauthRequestDto;
import com.example.dpm.auth.dto.OauthResponseDto;
import com.example.dpm.auth.service.JwtTokenService;
import com.example.dpm.auth.service.OauthService;
import com.example.dpm.member.dto.MemberDto;
import com.example.dpm.member.service.MemberService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class OauthController {
	private final OauthService oauthService;
	private final MemberService memberService;
	private final JwtTokenService jwtTokenService;

	@GetMapping("/login/oauth/kakao")
	public ResponseEntity<OauthResponseDto> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response)
			throws IOException {
		OauthResponseDto oauthResponseDto = new OauthResponseDto();

		// KakaoOauthService에서 Access/Refresh Token 발급받기
		Map<String, Object> tokenResponse = oauthService.getKakaoToken(code);

		String accessToken = (String) tokenResponse.get("access_token");
		String refreshToken = (String) tokenResponse.get("refresh_token");

		// 응답 헤더에 토큰을 추가
//        response.setHeader("accessToken", accessToken);
//        response.setHeader("refreshToken", refreshToken);

		// 리디렉션 URL 설정
//        String redirectUrl = "http://localhost:5173/";
//        response.sendRedirect(redirectUrl);

		// 응답 객체에 Access Token, Refresh Token 저장
		oauthResponseDto.setAccessToken(accessToken);
		oauthResponseDto.setRefreshToken(refreshToken);

		// oauthResponseDto.setCode(code);
		System.out.println("OauthController   " + refreshToken + "code: " + code);
		System.out.println("ffffffffffffffffffffff  " + ResponseEntity.ok(oauthResponseDto));

		String redirectUrl = "http://localhost:5173?accessToken=" + accessToken + "&refreshToken=" + refreshToken;
		response.sendRedirect(redirectUrl);
		return ResponseEntity.status(HttpStatus.OK).body(oauthResponseDto);
	}

//    @PostMapping("/login/oauth/{provider}")
//    public String login(@PathVariable("provider") String provider, @RequestBody OauthRequestDto oauthRequestDto,
//                                  HttpServletResponse response) {
//        OauthResponseDto oauthResponseDto = new OauthResponseDto();
//        String jwtToken = "";
//        
//        switch (provider) {
//            case "kakao":
//                // loginWithKakao 메서드가 TokenResponseDto 객체를 반환한다고 가정
//                 jwtToken = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), oauthRequestDto.getRefreshToken(), response);
//                 MemberDto memberDto = oauthService.UserInfo(oauthRequestDto.getAccessToken(), oauthRequestDto.getRefreshToken(), response);
//                break;
//
//            default:
//                // 예외 처리를 추가하여 잘못된 provider 요청에 대한 로그 확인
//                System.out.println("#OauthController: Provider: " + provider);
//                System.out.println("#OauthController: AccessToken: " + oauthRequestDto.getAccessToken());
//                throw new IllegalArgumentException("#OauthController: Unsupported provider: " + provider);
//        }
//        
//        
//        return ResponseEntity.status(HttpStatus.OK).body(Map<jwtTokenmemberDto>);
//    }

	@PostMapping("/login/oauth/{provider}")
	public ResponseEntity<MemberDto> login(@PathVariable("provider") String provider,
			@RequestBody OauthRequestDto oauthRequestDto, HttpServletResponse response) {

		OauthResponseDto oauthResponseDto = new OauthResponseDto();
		String jwtToken = "";
		MemberDto memberDto = null; // MemberDto를 초기화합니다.

		switch (provider) {
		case "kakao":
			// loginWithKakao 메서드가 TokenResponseDto 객체를 반환한다고 가정
			jwtToken = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), oauthRequestDto.getRefreshToken(),
					response);
			memberDto = oauthService.UserInfo(oauthRequestDto.getAccessToken(), oauthRequestDto.getRefreshToken(),
					response);
			break;

		default:
			// 예외 처리를 추가하여 잘못된 provider 요청에 대한 로그 확인
			System.out.println("#OauthController: Provider: " + provider);
			System.out.println("#OauthController: AccessToken: " + oauthRequestDto.getAccessToken());
			throw new IllegalArgumentException("#OauthController: Unsupported provider: " + provider);
		}

		// jwtToken을 키로, memberDto를 값으로 갖는 Map을 생성합니다.
		Map<String, MemberDto> responseMap = new HashMap<>();
		responseMap.put(jwtToken, memberDto); // jwtToken을 키로 사용

		return ResponseEntity.status(HttpStatus.OK).body(memberDto);
	}

}