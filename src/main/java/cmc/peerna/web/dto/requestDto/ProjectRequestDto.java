package cmc.peerna.web.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Date;


public class ProjectRequestDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProjectCreateDto{

        @NotBlank @Size(max = 15, message = "프로젝트 명은 1~15글자 사이여야 합니다.")
        private String projectName;
        private Date startDate;
        private Date endDate;
        @NotBlank @Size(max = 20, message = "프로젝트 설명은 1~20글자 사이여야 합니다.")
        private String introduce;
        @NotBlank
        private String openChattingLink;
        private String notionLink;
        private String githubLink;
    }
}
