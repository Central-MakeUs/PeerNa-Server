package cmc.peerna.feign.config;

import cmc.peerna.feign.exception.FeignClientExceptionErrorDecoder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class AppleSocialFeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor(){
        return template -> template.header("Content-Type", "application/json;charset=UTF-8");
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return  new FeignClientExceptionErrorDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
