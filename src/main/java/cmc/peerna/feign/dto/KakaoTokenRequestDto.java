package cmc.peerna.feign.dto;

import cmc.peerna.feign.info.KakaoInfo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoTokenRequestDto {
    private String code;
    private String client_id;
//    private String client_secret;
    private String redirect_uri;
    private final String grant_type = "authorization_code";

    public static KakaoTokenRequestDto newInstance(KakaoInfo kakaoInfo, String code) {
        return KakaoTokenRequestDto.builder()
                .client_id(kakaoInfo.getClientId())
//                .client_secret(kakaoInfo.getSecretKey())
                .redirect_uri(kakaoInfo.getRedirectUri())
                .code(code)
                .build();
    }

    @Override
    public String toString() {
        return
                "code=" + code + '&' +
                        "client_id=" + client_id + '&' +
//                        "client_secret=" + client_secret + '&' +
                        "redirect_uri=" + redirect_uri + '&' +
                        "grant_type=" + grant_type;
    }

}
