package cmc.peerna.apiResponse.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ResponseStatus implements BaseCode {

    _SUCCESS(OK, 2000, "성공!"),


    // 200번대 에러는 유저의 잘못된 요청 때문에 발생하는 에러 ex) 글자수 초과, db에 id:2인 member가 없는데 조회하는 경우

    // base 에러
    _BAD_REQUEST(OK,2100,"잘못된 요청입니다."),
    WRONG_GET_TEST(OK, 2102, "잘못된 GET 테스트 요청입니다."),
    MEMBER_COUNT_ZERO(OK, 2103, "해당 조건을 만족하는 멤버가 존재하지 않습니다."),
    WRONG_TEST_TYPE(OK, 2104, "TestType은 D,I,S,C 중 하나의 값이어야 합니다."),
    WRONG_PART(OK, 2105, "Part는 PLANNER, DESIGNER, FRONT_END, BACK_END, MARKETER, OTHER 중 하나의 값이어야 합니다."),

    // login 에러
    KAKAO_LOGIN_ERROR(OK, 2150, "카카오 로그인 도중 에러 발생"),

    // member 에러
    MEMBER_NOT_FOUND(OK, 2200, "존재하지 않는 유저입니다."),

    // Peertest 에러
    UUID_NOT_FOUND(OK, 2250, "잘못된 UUID 값입니다."),

    // Project 에러
    PROJECT_NOT_FOUND(OK, 2300, "존재하지 않는 프로젝트입니다."),
    PROJECT_COUNT_ZERO(OK, 2301, "조회된 프로젝트가 0개입니다."),
    ALREADY_EXIST_PROJECT_MEMBER(OK, 2302, "이미 해당 프로젝트에 참여중입니다."),
    PROJECT_SELF_INVITE(OK, 2303, "자신이 만든 프로젝트엔 참여할 수 없습니다."),



    // Notice 에러
    NOTICE_COUNT_ZERO(OK, 2350, "조회된 알림이 0개입니다."),



    // 400번대 에러

    _UNAUTHORIZED(UNAUTHORIZED,4001,"로그인이 필요합니다."),
    _FORBIDDEN(FORBIDDEN, 4003, "허용되지 않은 요청입니다."),
    FEIGN_CLIENT_ERROR_400(BAD_REQUEST, 4007, "feign에서 400번대 에러가 발생했습니다. 코드값이 잘못되었거나 이미 해당 코드를 통해 토큰 요청을 한 경우."),
    INVALID_TOKEN_EXCEPTION(UNAUTHORIZED,4008,"토큰이 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, 4009, "리프레쉬 토큰이 유효하지 않습니다. 다시 로그인 해주세요"),
    EXPIRED_JWT_EXCEPTION(UNAUTHORIZED, 4010, "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
    RE_LOGIN_EXCEPTION(UNAUTHORIZED, 4011, "모든 토큰이 만료되었습니다. 다시 로그인해주세요."),


    UNDER_PAGE_INDEX_ERROR(HttpStatus.BAD_REQUEST, 4012, "페이지 번호는 1 이상이여야 합니다."),
    //BAD_REQUEST
    OVER_PAGE_INDEX_ERROR(HttpStatus.BAD_REQUEST, 4013, "페이지 번호가 페이징 범위를 초과했습니다."),

    FAILED_TO_FIND_AVAILABLE_RSA(HttpStatus.UNAUTHORIZED, 4014, "Identity Token에서 유효한 값을 찾지 못했습니다"),
    WRONG_IDENTITY_TOKEN(BAD_REQUEST, 4015, "Identity Token의 형태가 잘못되었습니다."),

    INVALID_ACCESS_TOKEN(UNAUTHORIZED, 4016, "액세스 토큰이 없거나 유효하지 않습니다."),

    FCM_TOKEN_NOT_FOUND(BAD_REQUEST, 4017, "해당 유저의 FCM 토큰이 존재하지 않습니다."),

    WRONG_POST_TEST(BAD_REQUEST, 4101, "잘못된 POST 테스트 요청입니다."),

    // Test 관련 에러
    ANSWER_NOT_FOUND(BAD_REQUEST, 4200, "잘못된 답변 ID 값을 전달했습니다."),
    WRONG_ANSWER_COUNT(BAD_REQUEST, 4201, "답변 개수가 정확하게 18개가 아닙니다."),
    WRONG_TOTAL_ANSWER_COUNT(BAD_REQUEST, 4202, "피어테스트 답변 개수가 18의 배수가 아닙니다. 피어테스트 저장 과정에서 누락 발생"),


    // 프로젝트 및 알림 관련
    NOT_PROJECT_CREATOR(BAD_REQUEST, 4250, "자신이 만든 프로젝트가 아닙니다."),
    PROJECT_REQUEST_NOTICE_NOT_FOUND(BAD_REQUEST, 4251, "해당 유저가 프로젝트 참가 신청을 하지 않았습니다."),



    // 500번대 에러
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, 5000, "서버 에러, 관리자에게 문의 바랍니다."),
    FEIGN_CLIENT_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, 5001, "Inter server Error in feign client"),

    FCM_ACCESS_TOKEN_REQUEST_ERROR(INTERNAL_SERVER_ERROR, 5002, "서버 에러, FCM 서버에 AccessToken 요청할 때 에러 발생."),
    FCM_SEND_MESSAGE_ERROR(INTERNAL_SERVER_ERROR, 5003, "서버 에러, FCM 서버에 메시지를 전송할 때 에러 발생.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    @Override
    public Reason getReason() {
        return Reason.builder()
                .message(message)
                .code(code)
                .build();
    }

    @Override
    public Reason getReasonHttpStatus() {
        return Reason.builder()
                .message(message)
                .code(code)
                .httpStatus(httpStatus)
                .build();
    }
}
