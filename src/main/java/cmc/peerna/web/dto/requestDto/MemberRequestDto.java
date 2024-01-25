package cmc.peerna.web.dto.requestDto;

import cmc.peerna.domain.enums.Job;
import cmc.peerna.domain.enums.Part;
import cmc.peerna.domain.enums.PeerGrade;
import lombok.*;

import java.util.List;

public class MemberRequestDto {

    @Getter
    public static class OAuthDTO{
        private String accessToken;
    }

    @Getter
    public static class ReissueDTO {
        private String refreshToken;
    }

    @Getter
    public static class basicInfoDTO{
        private String name;
        private Job job;
        private Part part;
        private PeerGrade selfPeerGrade;
        private String oneLiner;
    }

    @Getter
    public static class selfTestDto{
        List<Long> answerIdList;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class profileUpdateDto{
        private Job job;
        private Part part;
        private String oneLiner;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class uuidRequestDto{
        private String uuid;
    }
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class IssueTokenDto {
        String refreshToken;
    }

}
