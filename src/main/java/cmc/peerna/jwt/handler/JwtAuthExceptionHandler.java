package cmc.peerna.jwt.handler;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.filter.ApiErrorResult;
import cmc.peerna.apiResponse.exception.handler.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JwtAuthExceptionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request,response);
        }catch (JwtAuthenticationException authException){
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            PrintWriter writer = response.getWriter();
            String errorCodeName = authException.getMessage();
            ResponseStatus commonStatus = ResponseStatus.valueOf(errorCodeName);

            ApiErrorResult apiErrorResult = ApiErrorResult.builder()
                    .code(commonStatus.getCode())
                    .message(commonStatus.getMessage())
                    .result(null)
                    .build();

            writer.write(apiErrorResult.toString());
            writer.flush();
            writer.close();
        }
    }
}