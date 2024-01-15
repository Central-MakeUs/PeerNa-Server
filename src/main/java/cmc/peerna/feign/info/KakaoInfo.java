package cmc.peerna.feign.info;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.kakao")
@Getter @Setter
public class KakaoInfo {

    private String baseUrl;
    private String clientId;
    private String redirectUri;
    private String secretKey;

    public String kakaoUrlInit(){
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", getClientId());
        params.put("redirect_uri", getRedirectUri());
        params.put("response_type", "code");

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        return getBaseUrl()
                +"/oauth/authorize"
                + "?"
                + paramStr;
    }


}
