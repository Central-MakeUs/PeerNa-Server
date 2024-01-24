package cmc.peerna.feign;

import cmc.peerna.feign.config.AppleSocialFeignConfig;
import cmc.peerna.feign.dto.ApplePublicKeyListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "apple-public-key-client", url = "https://appleid.apple.com/auth",configuration = AppleSocialFeignConfig.class)
@Component
public interface AppleSocialFeign {
    @GetMapping("/keys")
    ApplePublicKeyListDTO getApplePublicKeys();
}