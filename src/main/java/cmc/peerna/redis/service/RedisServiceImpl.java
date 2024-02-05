package cmc.peerna.redis.service;


import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.apiResponse.exception.handler.RefreshTokenException;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.SocialType;
import cmc.peerna.redis.domain.RefreshToken;
import cmc.peerna.redis.repository.RefreshTokenRepository;
import cmc.peerna.repository.MemberRepository;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisServiceImpl implements RedisService{
    Logger logger = LoggerFactory.getLogger(RedisService.class);

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.access-token-validity-in-seconds}000")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token-validity-in-seconds}000")
    private long refreshTokenValidityInMilliseconds;

    @Override
    @Transactional
    public RefreshToken generateRefreshToken(String socialId, SocialType socialType) {
        Member member =
                memberRepository
                        .findBySocialTypeAndSocialId(socialType, socialId)
                        .orElseThrow(() -> new RefreshTokenException(ResponseStatus.MEMBER_NOT_FOUND));

        String token = UUID.randomUUID().toString();

        Long memberId = member.getId();

        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime expireTime = currentTime.plus(refreshTokenValidityInMilliseconds, ChronoUnit.MILLIS);

        return refreshTokenRepository.save(
                RefreshToken.builder()
                        .memberId(memberId)
                        .token(token)
                        .expireTime(expireTime)
                        .build());
    }

    @Override
    public RefreshToken reGenerateRefreshToken(MemberRequestDto.ReissueDTO request) {
        logger.info("전달받은 리프레쉬 토큰 : " + request.getRefreshToken());
        if (request.getRefreshToken() == null)
            throw new MemberException(ResponseStatus.INVALID_TOKEN_EXCEPTION);
        RefreshToken findRefreshToken =
                refreshTokenRepository
                        .findById(request.getRefreshToken())
                        .orElseThrow(
                                () -> new RefreshTokenException(ResponseStatus.INVALID_REFRESH_TOKEN));
        LocalDateTime expireTime = findRefreshToken.getExpireTime();
        LocalDateTime current = LocalDateTime.now();

        LocalDateTime expireDeadLine = current.plusSeconds(accessTokenValidityInMilliseconds);

        Member member =
                memberRepository
                        .findById(findRefreshToken.getMemberId())
                        .orElseThrow(() -> new RefreshTokenException(ResponseStatus.MEMBER_NOT_FOUND));

        if (current.isAfter(expireTime)) {
            logger.error("이미 만료된 리프레시 토큰 발견");
            throw new RefreshTokenException(ResponseStatus.RE_LOGIN_EXCEPTION);
        }

        // 새로 발급할 accessToken보다 refreshToken이 먼저 만료 될 경우
        if (expireTime.isAfter(expireDeadLine)) {
            logger.info("기존 리프레시 토큰 발급");
            return findRefreshToken;
        } else {
            logger.info("accessToken보다 먼저 만료될 예정인 refresh 토큰 발견");
            deleteRefreshToken(request.getRefreshToken());
            return generateRefreshToken(member.getSocialId(), member.getSocialType());
        }
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        Optional<RefreshToken> target = refreshTokenRepository.findById(refreshToken);
        target.ifPresent(refreshTokenRepository::delete);
    }

}
