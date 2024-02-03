package cmc.peerna.apiResponse.exception.handler;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.GeneralException;

public class FcmException extends GeneralException {

    public FcmException(ResponseStatus code){
        super(code);
    }
}
