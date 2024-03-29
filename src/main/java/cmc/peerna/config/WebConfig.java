package cmc.peerna.config;

import cmc.peerna.jwt.handler.annotation.resolver.AuthMemberArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthMemberArgumentResolver authMemberArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolverList) {
        resolverList.add(authMemberArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 경로에 앞으로 만들 모든 CORS 정보를 적용한다
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://localhost:63343",
                        "http://localhost:3000",
                        "http://localhost:",
                        "http://localhost:8080",
                        "https://dev.peerna.me",
                        "https://www.peerna.me",
                        "https://peerna.me",
                        "http://localhost:5173",
                        "https://localhost:5173",
                        "https://ngrok-free.app",
                        "https://b6d2-58-76-161-229.ngrok-free.app/"

                )
                // 모든 HTTP Method를 허용한다.
                .allowedMethods("*", "PUT", "POST", "DELETE", "OPTIONS", "PATCH", "GET")
                // HTTP 요청의 Header에 어떤 값이든 들어갈 수 있도록 허용한다.
                .allowedHeaders("*")
                .exposedHeaders("Set-Cookie")
                // 자격증명 사용을 허용한다.
                // 해당 옵션 사용시 allowedOrigins를 * (전체)로 설정할 수 없다.
                .allowCredentials(true);
    }
}
