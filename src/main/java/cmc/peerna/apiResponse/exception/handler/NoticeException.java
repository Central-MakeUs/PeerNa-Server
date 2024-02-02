package cmc.peerna.apiResponse.exception.handler;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.GeneralException;

public class NoticeException extends GeneralException {

    public NoticeException(ResponseStatus code){
        super(code);
    }
}