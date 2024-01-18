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

    // login 에러
    KAKAO_LOGIN_ERROR(OK, 2150, "카카오 로그인 도중 에러 발생"),

    // member 에러
    MEMBER_NOT_FOUND(OK, 2200, "존재하지 않는 유저입니다."),


    // 400번대 에러

    _UNAUTHORIZED(UNAUTHORIZED,4001,"로그인이 필요합니다."),
    _FORBIDDEN(FORBIDDEN, 4003, "허용되지 않은 요청입니다."),
    FEIGN_CLIENT_ERROR_400(BAD_REQUEST, 4007, "feign에서 400번대 에러가 발생했습니다. 코드값이 잘못되었거나 이미 해당 코드를 통해 토큰 요청을 한 경우."),
    INVALID_TOKEN_EXCEPTION(UNAUTHORIZED,4008,"토큰이 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, 4009, "리프레쉬 토큰이 유효하지 않습니다. 다시 로그인 해주세요"),
    EXPIRED_JWT_EXCEPTION(UNAUTHORIZED, 4010, "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
    RE_LOGIN_EXCEPTION(UNAUTHORIZED, 4011, "모든 토큰이 만료되었습니다. 다시 로그인해주세요."),


    WRONG_POST_TEST(BAD_REQUEST, 4101, "잘못된 POST 테스트 요청입니다."),

    // Test 관련 에러
    ANSWER_NOT_FOUND(BAD_REQUEST, 4200, "잘못된 답변 ID 값을 전달했습니다."),
    WRONG_ANSWER_COUNT(BAD_REQUEST, 4201, "답변 개수가 정확하게 18개가 아닙니다."),

    // 500번대 에러
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, 5000, "서버 에러, 관리자에게 문의 바랍니다."),
    FEIGN_CLIENT_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, 5001, "Inter server Error in feign client");


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
