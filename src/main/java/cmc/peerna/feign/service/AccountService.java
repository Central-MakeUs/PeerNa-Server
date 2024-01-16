package cmc.peerna.feign.service;

import cmc.peerna.feign.KakaoInfoFeignClient;
import cmc.peerna.feign.KakaoLoginFeignClient;
import cmc.peerna.feign.dto.KakaoLoginPageRequestDto;
import cmc.peerna.feign.dto.KakaoTokenInfoResponseDto;
import cmc.peerna.feign.dto.KakaoTokenRequestDto;
import cmc.peerna.feign.dto.KakaoTokenResponseDto;
import cmc.peerna.feign.info.KakaoInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final KakaoLoginFeignClient kakaoLoginFeignClient;
    private final KakaoInfoFeignClient kakaoInfoFeignClient;
    private final KakaoInfo kakaoInfo;
    public HttpHeaders kakaoLoginRequestHeader(){
        return createHttpHeader(kakaoInfo.kakaoLoginUrlInit());
    }


    private static HttpHeaders createHttpHeader(String str) {
        try {
            URI uri = new URI(str);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(uri);
            return httpHeaders;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void gotoKakaoLogin() {
        String kakaoLoginPageRequestDto = KakaoLoginPageRequestDto.newInstance(kakaoInfo).toString();
        System.out.println("테스트 : "  + kakaoLoginPageRequestDto);

            kakaoLoginFeignClient.goToKakaoLoginPage(kakaoLoginPageRequestDto);
    }


    // 인가 코드를 통해 카카오 유저 정보 알아내는 함수
    public KakaoTokenInfoResponseDto getKakaoUserInfo(String code) {
        String kakaoTokenRequestDto = KakaoTokenRequestDto.newInstance(kakaoInfo, code).toString();
        String kakaoAccessToken = getKakaoAccessToken(kakaoTokenRequestDto);
        return getKakaoInfo(kakaoAccessToken);
    }

    // 인가 코드를 통해 카카오 서버의 액세스 토큰 알아내는 함수
    public String getKakaoAccessToken(String code){
        KakaoTokenResponseDto kakaoTokenResponseDto = kakaoLoginFeignClient.getToken(code);
        return kakaoTokenResponseDto.getAccess_token();
    }

    // 액세스 토큰을 통해 카카오 유저 정보 알아내는 함수
    public KakaoTokenInfoResponseDto getKakaoInfo(String accessToken) {
        return kakaoInfoFeignClient.getInfo(accessToken);
    }

}
