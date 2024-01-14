package cmc.peerna.feign;

import cmc.peerna.feign.config.KakaoFeignConfig;
import cmc.peerna.feign.dto.KakaoMemberInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoInfoFeignClient", url = "https://kapi.kakao.com", configuration = KakaoFeignConfig.class)
@Component
public interface KakaoInfoFeignClient {

    @GetMapping("/v2/user/me")
    KakaoMemberInfo getInfo(@RequestHeader(name = "Authorization") String Authorization);
}
