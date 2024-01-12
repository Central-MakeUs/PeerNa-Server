package cmc.peerna.apiResponse.response;

import cmc.peerna.apiResponse.code.BaseCode;
import cmc.peerna.apiResponse.code.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "result"})
//@Schema(description = "기본 응답")
public class ResponseDto<T> {

    private final Integer code;
    private final String message;
    private T result;


    // 성공한 경우 응답 생성

    public static <T> ResponseDto<T> of(T result){
        return new ResponseDto<>(2000 , ResponseStatus._SUCCESS.getMessage(), result);
    }

    public static <T> ResponseDto<T> of(BaseCode code, T result){
        ResponseDto<T> tResponseDto = new ResponseDto<>(code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), result);
        return tResponseDto;
    }

    // 실패한 경우 응답 생성
    public static <T> ResponseDto<T> onFailure(Integer code, String message, T data){
        return new ResponseDto<>(code, message, data);
    }
}
