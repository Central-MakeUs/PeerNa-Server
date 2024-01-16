package cmc.peerna.feign.service;

import cmc.peerna.feign.KakaoInfoFeignClient;
import cmc.peerna.feign.KakaoLoginFeignClient;
import cmc.peerna.feign.dto.KakaoTokenInfoResponseDto;
import cmc.peerna.feign.dto.KakaoTokenRequestDto;
import cmc.peerna.feign.dto.KakaoTokenResponseDto;
import cmc.peerna.feign.info.KakaoInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
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



    // 인가 코드를 통해 카카오 유저 정보 알아내는 함수
    public KakaoTokenInfoResponseDto getKakaoUserInfo(String code) {
        String kakaoTokenRequestDto = KakaoTokenRequestDto.newInstance(kakaoInfo, code).toString();
        log.info("요청 정보 DTO : " + kakaoTokenRequestDto);
        String kakaoAccessToken = getKakaoAccessToken(kakaoTokenRequestDto);
        return getKakaoInfo(kakaoAccessToken);
    }

    // 인가 코드를 통해 카카오 서버의 액세스 토큰 알아내는 함수
    public String getKakaoAccessToken(String code){
        log.info("Feign으로 액세스 토큰  요청 시작");
        log.info("토큰 추출할 때 카카오 서버에 요청할 때 보내는 정보 : " + code);
        KakaoTokenResponseDto kakaoTokenResponseDto = kakaoLoginFeignClient.getToken(code);
        log.info("카카오 서버에서 얻은 토큰 값 :  " + kakaoTokenResponseDto.getAccess_token());
        return kakaoTokenResponseDto.getAccess_token();
    }

    // 액세스 토큰을 통해 카카오 유저 정보 알아내는 함수
    public KakaoTokenInfoResponseDto getKakaoInfo(String accessToken) {
        return kakaoInfoFeignClient.getInfo(accessToken);
    }

}
