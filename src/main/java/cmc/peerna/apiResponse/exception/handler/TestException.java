package cmc.peerna.apiResponse.exception.handler;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.GeneralException;

public class TestException extends GeneralException {
    public TestException(ResponseStatus code){
        super(code);
    }
}
