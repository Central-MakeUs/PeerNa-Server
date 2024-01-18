package cmc.peerna.apiResponse.exception.handler;


import cmc.peerna.apiResponse.code.BaseCode;
import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.GeneralException;

public class CustomFeignClientException extends GeneralException {

    public CustomFeignClientException(ResponseStatus code){
        super(code);
    }
}
