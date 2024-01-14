package cmc.peerna.converter;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.SocialType;

public class MemberConverter {
    public static Member toMember(String socialId, SocialType socialType) {
        return Member.builder()
                .socialId((socialId))
                .socialType(socialType)
                .build();
    }
}
