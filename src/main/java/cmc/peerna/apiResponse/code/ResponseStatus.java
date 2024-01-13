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

    // base 에러
    _BAD_REQUEST(BAD_REQUEST,2100,"잘못된 요청입니다."),
    WRONG_POST_TEST(BAD_REQUEST, 2101, "잘못된 POST 테스트 요청입니다."),
    WRONG_GET_TEST(BAD_REQUEST, 2102, "잘못된 GET 테스트 요청입니다."),

    // member 에러
    NOT_EXIST_MEMBER(BAD_REQUEST, 2200, "존재하지 않는 유저입니다.");

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
