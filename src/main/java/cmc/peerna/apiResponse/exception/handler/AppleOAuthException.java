package cmc.peerna.apiResponse.exception.handler;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.GeneralException;

public class AppleOAuthException extends GeneralException {
    public AppleOAuthException(ResponseStatus code) {
        super(code);

    }
}
