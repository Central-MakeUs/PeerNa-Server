package cmc.peerna.apiResponse.exception.handler;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.GeneralException;

public class ProjectException extends GeneralException {

    public ProjectException(ResponseStatus code){
        super(code);
    }
}
