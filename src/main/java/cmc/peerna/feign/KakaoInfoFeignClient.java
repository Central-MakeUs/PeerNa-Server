package cmc.peerna.feign;

import cmc.peerna.feign.dto.KakaoTokenInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoInfoFeignClient", url = "https://kapi.kakao.com", configuration = FeignClientProperties.FeignClientConfiguration.class)
@Component
public interface KakaoInfoFeignClient {

    @GetMapping("/v1/user/access_token_info")
    KakaoTokenInfoResponseDto getInfo(@RequestHeader(name = "Authorization") String Authorization);

}
