package cmc.peerna.feign.dto;

import cmc.peerna.feign.info.KakaoInfo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoLoginPageRequestDto {
    private String client_id;
    private String redirect_uri;
    private String response_type;

    public static KakaoLoginPageRequestDto newInstance(KakaoInfo kakaoInfo) {
        return KakaoLoginPageRequestDto.builder()
                .client_id(kakaoInfo.getClientId())
                .redirect_uri(kakaoInfo.getRedirectUri())
                .response_type("code")
                .build();
    }

    @Override
    public String toString() {
        return
                "response_type=" + response_type + '&' +
                        "client_id=" + client_id + '&' +
                        "redirect_uri=" + redirect_uri;
    }
}
