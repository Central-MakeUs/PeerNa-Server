package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.service.MemberService;
import cmc.peerna.web.dto.requestDto.RootRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "2000",description = "OK 성공"),
        @ApiResponse(responseCode = "5000",description = "서버 에러, 로빈에게 알려주세요."),
})
@Tag(name = "테스트, 기타 API", description = "테스트, 기타 API 목록입니다.")
public class RootController {

    private final MemberService memberService;

    @GetMapping("/health")
    public String healthCheck() {
        return "I'm healthy!";
    }

    @Operation(summary = "POST 요청 TEST API ✔️", description = "POST 요청 테스트용 API입니다. <br> body에 \"peerna\" 를 넣은 경우 4101 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "4101",description = "BAD_REQUEST, body에 \"peerna\" 를 넣은 경우")
    })
    @PostMapping("/test/ping")
    public ResponseDto<String> postTest(@RequestBody RootRequestDto.PostTestDto body) {
        if (body.getBody().equals("peerna")) {
            throw new MemberException(ResponseStatus.WRONG_POST_TEST);
        }
        return ResponseDto.of(body.getBody());
    }

    // TEST API
    @GetMapping("/test/{member-id}")
    @Operation(summary = "GET 요청 TEST API ✔️", description = "GET 요청 테스트용 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200",description = "BAD_REQUEST, 존재하지 않는 유저입니다.")
    })
    public ResponseDto<MemberResponseDto.MemberGetTestDto> searchMember(@PathVariable(name = "member-id") Long memberId) {
        MemberResponseDto.MemberGetTestDto memberDto = memberService.findMember(memberId);
        return ResponseDto.of(memberDto);
    }
}
