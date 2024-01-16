package cmc.peerna.redis.service;

import cmc.peerna.domain.enums.SocialType;
import cmc.peerna.redis.domain.RefreshToken;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;

public interface RedisService {
    RefreshToken generateRefreshToken(String socialId, SocialType socialType);

    // accessToken 만료 시 발급 혹은 그대로 반환
    RefreshToken reGenerateRefreshToken(MemberRequestDto.ReissueDTO request);
    void deleteRefreshToken(String refreshToken);
}
