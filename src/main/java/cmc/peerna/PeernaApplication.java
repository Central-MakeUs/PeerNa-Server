package cmc.peerna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableFeignClients
@SpringBootApplication
@EnableRedisRepositories
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class PeernaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeernaApplication.class, args);
	}

}
