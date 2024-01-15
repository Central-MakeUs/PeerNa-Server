package cmc.peerna.feign.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoTokenInfoResponseDto {
    private String id;
    private String expires_in;
    private String app_id;
}
