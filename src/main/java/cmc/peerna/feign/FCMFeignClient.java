package cmc.peerna.feign;

import cmc.peerna.feign.config.FCMFeignConfig;
import cmc.peerna.feign.dto.FCMResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "FCMFeign", url = "https://fcm.googleapis.com", configuration = FCMFeignConfig.class)
@Component
public interface FCMFeignClient {

    @PostMapping("/v1/projects/peerna-68c2d/messages:send")
    FCMResponseDto getFCMResponse(@RequestHeader("Authorization") String token, @RequestBody String fcmAOSMessage);
}
