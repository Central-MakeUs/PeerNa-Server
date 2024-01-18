package cmc.peerna.web.dto.requestDto;

import cmc.peerna.domain.enums.Job;
import cmc.peerna.domain.enums.Part;
import cmc.peerna.domain.enums.PeerGrade;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

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
}
