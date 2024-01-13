package cmc.peerna.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

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
                        "https://localhost:5173"


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
