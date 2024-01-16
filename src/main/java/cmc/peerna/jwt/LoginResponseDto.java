package cmc.peerna.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class LoginResponseDto {
    private Long memberId;
    private String accessToken;
    private String refreshToken;
}
