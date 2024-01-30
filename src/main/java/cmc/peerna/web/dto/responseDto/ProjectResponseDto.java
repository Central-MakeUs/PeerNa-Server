package cmc.peerna.web.dto.responseDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Date;
import java.util.List;

public class ProjectResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProjectDetailDto{
        private Long projectId;
        private String projectName;
        private String introduce;
        private Date startDate;
        private Date endDate;
        private String openChattingLink;
        private String notionLink;
        private String githubLink;
        private MemberResponseDto.memberSimpleProfileDto creatorSimpleProfileDto;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProjectSimpleProfileDto{
        private Long projectId;
        private String projectName;
        private String introduce;
        private Date startDate;
        private Date endDate;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProjectPageDto{
        List<ProjectSimpleProfileDto> projectList;
        Long totalElements;
        Integer currentPageElements;
        Integer totalPage;
        Boolean isFirst;
        Boolean isLast;
    }
}
