package cmc.peerna.apiResponse.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ResponseStatus implements BaseCode {

    _SUCCESS(OK, 2000, "성공!"),

    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, 5000, "서버 에러, 관리자에게 문의 바랍니다."),

    // 200번대 에러는 유저의 잘못된 요청 때문에 발생하는 에러 ex) 글자수 초과, db에 id:2인 member가 없는데 조회하는 경우

    // base 에러
    _BAD_REQUEST(OK,2100,"잘못된 요청입니다."),
    WRONG_GET_TEST(OK, 2102, "잘못된 GET 테스트 요청입니다."),

    // member 에러
    NOT_EXIST_MEMBER(OK, 2200, "존재하지 않는 유저입니다."),



    // 400번대 에러

    _UNAUTHORIZED(UNAUTHORIZED,4001,"로그인이 필요합니다."),
    _FORBIDDEN(FORBIDDEN, 4003, "허용되지 않은 요청입니다."),
    INVALID_TOKEN_EXCEPTION(UNAUTHORIZED,4008,"토큰이 올바르지 않습니다." ),

    FEIGN_CLIENT_ERROR_400(HttpStatus.BAD_REQUEST, 4009, "feign에서 400번대 에러가 발생했습니다."),

    WRONG_POST_TEST(BAD_REQUEST, 4101, "잘못된 POST 테스트 요청입니다."),

    // 500번대 에러
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
