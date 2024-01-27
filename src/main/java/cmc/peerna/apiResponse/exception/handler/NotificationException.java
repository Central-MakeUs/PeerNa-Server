package cmc.peerna.apiResponse.exception.handler;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.GeneralException;

public class NotificationException extends GeneralException {
    public NotificationException(ResponseStatus code){
        super(code);
    }
}
