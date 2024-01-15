package cmc.peerna.feign.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoTokenResponseDto {
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;
    private String id_token;
    private String refresh_token_expires_in;

    public String getAccess_token() {
        return "Bearer "+access_token;
    }
}

