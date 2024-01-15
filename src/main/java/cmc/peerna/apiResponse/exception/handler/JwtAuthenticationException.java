package cmc.peerna.apiResponse.exception.handler;

import cmc.peerna.apiResponse.code.ResponseStatus;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(ResponseStatus code){
        super(code.name());
    }
}
