package cmc.peerna.feign.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoMemberInfo {
    Long id;
    LocalDateTime connected_at;

}