package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.apiResponse.response.PageResponseDto;
import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.Project;
import cmc.peerna.domain.enums.NoticeGroup;
import cmc.peerna.domain.enums.NoticeType;
import cmc.peerna.fcm.service.FcmService;
import cmc.peerna.jwt.handler.annotation.AuthMember;
import cmc.peerna.service.MemberService;
import cmc.peerna.service.NoticeService;
import cmc.peerna.service.ProjectService;
import cmc.peerna.validation.annotation.CheckPage;
import cmc.peerna.web.dto.requestDto.ProjectRequestDto;
import cmc.peerna.web.dto.requestDto.RootRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import cmc.peerna.web.dto.responseDto.ProjectResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "2000", description = "OK 성공"),
        @ApiResponse(responseCode = "4008", description = "토큰이 올바르지 않습니다."),
        @ApiResponse(responseCode = "4009", description = "리프레쉬 토큰이 유효하지 않습니다. 다시 로그인 해주세요"),
        @ApiResponse(responseCode = "4010", description = "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
        @ApiResponse(responseCode = "4011", description = "모든 토큰이 만료되었습니다. 다시 로그인해주세요."),
        @ApiResponse(responseCode = "5000", description = "서버 에러, 로빈에게 알려주세요."),
})
@Tag(name = "Project 관련 API 목록", description = "Project 관련 API 목록입니다.")

public class ProjectController {
    private final ProjectService projectService;
    private final NoticeService noticeService;
    private final FcmService fcmService;
    private final MemberService memberService;

    private final String fcmTitle = "[PeerNa]";

    @Operation(summary = "프로젝트 생성 API ✔️🔑", description = "새 프로젝트 생성하는 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PostMapping("/project")
    public ResponseDto<MemberResponseDto.MemberStatusDto> saveMemberInfo(@AuthMember Member member, @RequestBody @Valid ProjectRequestDto.ProjectCreateDto request) {
        projectService.newProject(member, request);
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "프로젝트 생성 완료"));
    }

//    @Operation(summary = "전체 프로젝트 조회 API ✔️🔑", description = "전체 프로젝트 조회 API입니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "4012", description = "BAD_REQUEST , 페이지 번호는 1 이상이여야 합니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
//            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST , 페이지 번호가 페이징 범위를 초과했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
//            @ApiResponse(responseCode = "2301", description = "OK , 프로젝트가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
//    })
//    @Parameters({
//            @Parameter(name = "member", hidden = true)
//
//    })
//    @GetMapping("/project")
//    public ResponseDto<ProjectResponseDto.ProjectPageDto> getAllProject(@AuthMember Member member, @CheckPage @RequestParam(name = "page") Integer page) {
//        if (page == null)
//            page = 1;
//        else if (page < 1)
//            throw new MemberException(ResponseStatus.UNDER_PAGE_INDEX_ERROR);
//        page -= 1;
//
//        ProjectResponseDto.ProjectPageDto allProject = projectService.getAllProject(page);
//        return ResponseDto.of(allProject);
//    }

    @Operation(summary = "전체 프로젝트 조회 API ✔️🔑", description = "전체 프로젝트 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "4012", description = "BAD_REQUEST , 페이지 번호는 1 이상이여야 합니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST , 페이지 번호가 페이징 범위를 초과했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2301", description = "OK , 조회된 프로젝트가 0개입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)

    })
    @GetMapping("/project")
    public PageResponseDto<List<ProjectResponseDto.ProjectSimpleProfileDto>> getAllProject(@AuthMember Member member, @CheckPage @RequestParam(name = "page") Integer page) {
        if (page == null)
            page = 1;
        else if (page < 1)
            throw new MemberException(ResponseStatus.UNDER_PAGE_INDEX_ERROR);
        page -= 1;

        ProjectResponseDto.ProjectPageDto allProject = projectService.getAllProject(page);

        List<ProjectResponseDto.ProjectSimpleProfileDto> projectList;
        projectList = allProject.getProjectList();

        RootRequestDto.PageRequestDto pageRequestDto = RootRequestDto.PageRequestDto.builder()
                .totalElements(allProject.getTotalElements())
                .currentPageElements(allProject.getCurrentPageElements())
                .totalPage(allProject.getTotalPage())
                .isFirst(allProject.getIsFirst())
                .isLast(allProject.getIsLast())
                .build();

        return PageResponseDto.of(projectList, pageRequestDto);
    }


    @Operation(summary = "내가 생성한 프로젝트 조회 API ✔️🔑", description = "내가 생성한 프로젝트 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "4012", description = "BAD_REQUEST , 페이지 번호는 1 이상이여야 합니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST , 페이지 번호가 페이징 범위를 초과했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2301", description = "OK , 프로젝트가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/project/created")
    public ResponseDto<ProjectResponseDto.ProjectPageDto> getCreatedProject(@AuthMember Member member, @CheckPage @RequestParam(name = "page") Integer page) {
        if (page == null)
            page = 1;
        else if (page < 1)
            throw new MemberException(ResponseStatus.UNDER_PAGE_INDEX_ERROR);
        page -= 1;

        ProjectResponseDto.ProjectPageDto allProject = projectService.getMyProject(member, page);
        return ResponseDto.of(allProject);
    }

    @Operation(summary = "내 프로젝트 조회 API ✔️🔑", description = "내 프로젝트 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "4012", description = "BAD_REQUEST , 페이지 번호는 1 이상이여야 합니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST , 페이지 번호가 페이징 범위를 초과했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2301", description = "OK , 조회된 프로젝트가 0개입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/project/my")
    public PageResponseDto<List<ProjectResponseDto.ProjectSimpleProfileDto>> getMyProject(@AuthMember Member member, @CheckPage @RequestParam(name = "page") Integer page) {
        if (page == null)
            page = 1;
        else if (page < 1)
            throw new MemberException(ResponseStatus.UNDER_PAGE_INDEX_ERROR);
        page -= 1;

        ProjectResponseDto.ProjectPageDto myProject = projectService.getMyProject(member, page);

        List<ProjectResponseDto.ProjectSimpleProfileDto> projectList;
        projectList = myProject.getProjectList();

        RootRequestDto.PageRequestDto pageRequestDto = RootRequestDto.PageRequestDto.builder()
                .totalElements(myProject.getTotalElements())
                .currentPageElements(myProject.getCurrentPageElements())
                .totalPage(myProject.getTotalPage())
                .isFirst(myProject.getIsFirst())
                .isLast(myProject.getIsLast())
                .build();

        return PageResponseDto.of(projectList, pageRequestDto);
    }

    @Operation(summary = "프로젝트 상세 페이지 조회 API ✔️🔑", description = "프로젝트 상세 페이지 조회 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/project/{project-id}")
    public ResponseDto<ProjectResponseDto.ProjectDetailDto> getProjectDetail(@AuthMember Member member, @PathVariable(name = "project-id") Long projectId) {
        ProjectResponseDto.ProjectDetailDto projectDetailInfo = projectService.getProjectDetailInfo(projectId);
        return ResponseDto.of(projectDetailInfo);
    }

    @Operation(summary = "프로젝트 초대 링크 조회 API ✔️🔑", description = "프로젝트 초대 링크 조회 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/project/{project-id}/invite/link")
    public ResponseDto<String> getProjectInviteLinkResponse(@AuthMember Member member, @PathVariable(name = "project-id") Long projectId) {
        String projectCreatorName = projectService.findProjectCreator(projectId);
        return ResponseDto.of(projectCreatorName);
    }

    @Operation(summary = "링크로 초대된 프로젝트 자세히 알아보기 API ✔️🔑", description = "링크로 초대된 프로젝트 자세히 알아보기 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/project/{project-id}/invite/detail")
    public ResponseDto<ProjectResponseDto.ProjectDetailDto> getProjectInviteDetail(@AuthMember Member member, @PathVariable(name = "project-id") Long projectId) {
        ProjectResponseDto.ProjectDetailDto projectDetailInfo = projectService.getProjectDetailInfo(projectId);
        return ResponseDto.of(projectDetailInfo);
    }

    @Operation(summary = "링크로 초대된 프로젝트 - 초대 수락 API ✔️🔑", description = "링크로 초대된 프로젝트 - 초대 수락 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2300", description = "OK , 프로젝트가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2302", description = "OK , 이미 해당 프로젝트에 참여중입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2303", description = "OK , 자신이 만든 프로젝트엔 참여할 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @PostMapping("/project/{project-id}/invite/accept")
    public ResponseDto<MemberResponseDto.MemberStatusDto> acceptProjectLinkInvite(@AuthMember Member member, @PathVariable(name = "project-id") Long projectId) {
        projectService.checkProjectSelfInvite(projectId, member.getId());
        projectService.checkExistProjectMember(projectId, member.getId());

        projectService.saveNewProjectMember(projectId, member.getId());
        Project project = projectService.findById(projectId);

        String messageContents = member.getName() + "님이 \'"+project.getName()+"\' 참여 제안을 수락했어요.";
        fcmService.sendFcmMessage(project.getCreator(), fcmTitle, messageContents);
        noticeService.createNotice(member, project.getCreator().getId(), NoticeGroup.PROJECT, NoticeType.ACCEPT_PROJECT_INVITATION, projectId,messageContents);



        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "프로젝트 초대 수락"));
    }

    @Operation(summary = "링크로 초대된 프로젝트 - 초대 거절 API ✔️🔑", description = "링크로 초대된 프로젝트 - 초대 거절 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2300", description = "OK , 프로젝트가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2302", description = "OK , 이미 해당 프로젝트에 참여중입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2303", description = "OK , 자신이 만든 프로젝트엔 참여할 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @PostMapping("/project/{project-id}/invite/decline")
    public ResponseDto<MemberResponseDto.MemberStatusDto> declineProjectLinkInvite(@AuthMember Member member, @PathVariable(name = "project-id") Long projectId) {
        projectService.checkProjectSelfInvite(projectId, member.getId());
        projectService.checkExistProjectMember(projectId, member.getId());

        Project project = projectService.findById(projectId);

        String messageContents = member.getName() + "님이 \'"+project.getName()+"\' 참여 제안을 거절했어요.";
        fcmService.sendFcmMessage(project.getCreator(), fcmTitle, messageContents);
        noticeService.createNotice(member, project.getCreator().getId(), NoticeGroup.PROJECT, NoticeType.DECLINE_PROJECT_INVITATION, projectId,messageContents);

        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "프로젝트 초대 거절"));
    }


    @Operation(summary = "동료 상세 - 내가 만든 프로젝트에 동료 초대 API ✔️🔑", description = "동료 상세 - 내가 만든 프로젝트에 동료 초대 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우."),
            @ApiResponse(responseCode = "2300", description = "OK , 프로젝트가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2302", description = "OK , 이미 해당 프로젝트에 참여중입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4250", description = "BAD_REQUEST , 자신이 만든 프로젝트에만 초대할 수 있습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/project/{project-id}/invite/{peer-id}")
    public ResponseDto<MemberResponseDto.MemberStatusDto> invitePeerToMyProject(@AuthMember Member member, @PathVariable(name = "project-id") Long projectId, @PathVariable(name = "peer-id") Long peerId) {

        projectService.checkExistProjectMember(projectId, peerId);
        projectService.checkProjectCreator(projectId, member);

        Project project = projectService.findById(projectId);

        String messageContents = "\'"+project.getName()+"\' 참여 제안이 있어요.";
        fcmService.sendFcmMessage(memberService.findById(peerId), fcmTitle, messageContents);
        noticeService.createNotice(member, peerId, NoticeGroup.PROJECT, NoticeType.INVITE_TO_PROJECT, projectId,messageContents);
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "프로젝트 초대 완료"));
    }


    @Operation(summary = "[프로젝트 참여 신청한 입장] - 프로젝트 참가 신청 API ✔️🔑", description = "프로젝트 참여 신청한 입장 - 프로젝트 참가 신청 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2300", description = "OK , 프로젝트가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2302", description = "OK , 이미 해당 프로젝트에 참여중입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @PostMapping("/project/{project-id}/request-join")
    public ResponseDto<MemberResponseDto.MemberStatusDto> requestEnterProject(@AuthMember Member member, @PathVariable(name = "project-id") Long projectId) {

        projectService.checkExistProjectMember(projectId, member.getId());

        Project project = projectService.findById(projectId);

        String messageContents = member.getName()+ "님이 \'"+project.getName()+"\' 에 참여하고 싶어해요.";
        fcmService.sendFcmMessage(project.getCreator(), fcmTitle, messageContents);
        noticeService.createNotice(member, project.getCreator().getId(), NoticeGroup.PROJECT, NoticeType.REQUEST_JOIN_PROJECT, projectId,messageContents);
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "프로젝트 참가 신청 완료"));
    }


    @Operation(summary = "[참여 신청 받은 입장] - 프로젝트 참가 신청 수락 API ✔️🔑", description = "[참여 신청 받은 입장] - 프로젝트 참가 신청 수락 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우."),
            @ApiResponse(responseCode = "2300", description = "OK , 프로젝트가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2302", description = "OK , 이미 해당 프로젝트에 참여중입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4250", description = "BAD_REQUEST , 자신이 만든 프로젝트가 아닙니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4251", description = "BAD_REQUEST , 해당 유저가 프로젝트 참가 신청을 하지 않았습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),

    })
    @PostMapping("/project/{project-id}/request-join/{peer-id}/accept")
    public ResponseDto<MemberResponseDto.MemberStatusDto> acceptRequest(@AuthMember Member member, @PathVariable(name = "project-id") Long projectId, @PathVariable(name = "peer-id") Long peerId) {

        projectService.checkExistProjectMember(projectId, peerId);
        projectService.checkProjectCreator(projectId, member);

        // 참가 신청이 있었는지 확인
        noticeService.existsProjectJoinRequestNotice(member.getId(), peerId);

        Project project = projectService.findById(projectId);
        projectService.saveNewProjectMember(projectId, peerId);

        String messageContents = "\'" + project.getName() + "\' 참가 신청이 수락 되었어요.";
        fcmService.sendFcmMessage(memberService.findById(peerId), fcmTitle, messageContents);
        noticeService.createNotice(member, project.getCreator().getId(), NoticeGroup.PROJECT, NoticeType.ACCEPT_PROJECT_JOIN_REQUEST, projectId, messageContents);
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "프로젝트 참가 신청 수락 완료"));
    }

    @Operation(summary = "[참여 신청 받은 입장] - 프로젝트 참가 신청 거절 API ✔️🔑", description = "[참여 신청 받은 입장] - 프로젝트 참가 신청 거절 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우."),
            @ApiResponse(responseCode = "2300", description = "OK , 프로젝트가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2302", description = "OK , 이미 해당 프로젝트에 참여중입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4250", description = "BAD_REQUEST , 자신이 만든 프로젝트가 아닙니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4251", description = "BAD_REQUEST , 해당 유저가 프로젝트 참가 신청을 하지 않았습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @PostMapping("/project/{project-id}/request-join/{peer-id}/decline")
    public ResponseDto<MemberResponseDto.MemberStatusDto> declineRequest(@AuthMember Member member, @PathVariable(name = "project-id") Long projectId, @PathVariable(name = "peer-id") Long peerId) {

        projectService.checkExistProjectMember(projectId, peerId);
        projectService.checkProjectCreator(projectId, member);

        // 참가 신청이 있었는지 확인
        noticeService.existsProjectJoinRequestNotice(member.getId(), peerId);

        Project project = projectService.findById(projectId);

        String messageContents = "\'" + project.getName() + "\' 참가 신청이 거절 되었어요.";
        fcmService.sendFcmMessage(memberService.findById(peerId), fcmTitle, messageContents);
        noticeService.createNotice(member, project.getCreator().getId(), NoticeGroup.PROJECT, NoticeType.DECLINE_PROJECT_JOIN_REQUEST, projectId, messageContents);
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "프로젝트 참가 신청 거절 완료"));
    }
}
