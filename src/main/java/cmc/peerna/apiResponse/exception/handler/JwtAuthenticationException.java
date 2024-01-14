package cmc.peerna.apiResponse.exception.handler;

import cmc.peerna.apiResponse.code.ResponseStatus;
import codebase.SpringCodeBase.apiPayload.code.ErrorStatus;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(ResponseStatus code){
        super(code.name());
    }
}
