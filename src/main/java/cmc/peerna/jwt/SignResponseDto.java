package cmc.peerna.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class SignResponseDto {
    private String token;
}
