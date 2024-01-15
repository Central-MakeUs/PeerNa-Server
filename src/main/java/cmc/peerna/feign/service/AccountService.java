package cmc.peerna.feign.service;

import cmc.peerna.feign.info.KakaoInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final KakaoInfo kakaoInfo;
    public HttpHeaders kakaoLogin(){
        return createHttpHeader(kakaoInfo.kakaoUrlInit());
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
}
