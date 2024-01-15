package cmc.peerna.feign;

import cmc.peerna.feign.config.KakaoFeignConfig;
import cmc.peerna.feign.dto.KakaoTokenResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kakaoLoginFeignClient", url = "https://kauth.kakao.com", configuration = KakaoFeignConfig.class)
@Component
public interface KakaoLoginFeignClient {


    @PostMapping(value = "/oauth/token")
    KakaoTokenResponseDto getToken(@RequestBody String code);
}
