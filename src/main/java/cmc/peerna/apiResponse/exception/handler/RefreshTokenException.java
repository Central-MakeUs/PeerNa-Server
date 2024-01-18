package cmc.peerna.apiResponse.exception.handler;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.GeneralException;

public class RefreshTokenException extends GeneralException {
    public RefreshTokenException(ResponseStatus code){
        super(code);
    }
}
