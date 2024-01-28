package cmc.peerna.apiResponse.exception.handler;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.GeneralException;

public class RootException extends GeneralException {

    public RootException(ResponseStatus code){
        super(code);
    }
}
