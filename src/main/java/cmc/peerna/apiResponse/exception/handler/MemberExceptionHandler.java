package cmc.peerna.apiResponse.exception.handler;

import cmc.peerna.apiResponse.code.BaseCode;
import cmc.peerna.apiResponse.exception.GeneralException;

public class MemberExceptionHandler extends GeneralException {

    public MemberExceptionHandler(BaseCode errorCommonStatus) {
        super(errorCommonStatus);
    }
}
