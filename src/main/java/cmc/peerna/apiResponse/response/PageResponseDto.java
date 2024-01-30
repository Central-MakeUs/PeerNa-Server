package cmc.peerna.apiResponse.response;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.web.dto.requestDto.RootRequestDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//@JsonPropertyOrder({"code", "message", "result"})
public class PageResponseDto<T> {
    private final Integer code;
    private final String message;
    private T result;
    private RootRequestDto.PageRequestDto pageRequestDto;
//    private Long totalElements;
//    private Integer currentPageElements;
//    private Integer totalPage;
//    private Boolean isFirst;
//    private Boolean isLast;


    public static <T> PageResponseDto<T> of(T result, RootRequestDto.PageRequestDto requestDto){
        return new PageResponseDto<>(2000 , ResponseStatus._SUCCESS.getMessage(), result, requestDto);
    }

}
