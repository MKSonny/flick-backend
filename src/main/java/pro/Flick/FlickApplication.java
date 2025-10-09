package pro.Flick;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
//import pro.Flick.aop.LogTraceAspect;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pro.Flick.aop.LogTraceAspect;
import pro.Flick.controller.DbInit;

@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
@EnableJpaAuditing
@EnableScheduling
//@Import(LogTraceAspect.class)
public class FlickApplication {
	private final DbInit dbInit;

	public static void main(String[] args) {
		SpringApplication.run(FlickApplication.class, args);
	}

    @Bean
    public InMemoryHttpExchangeRepository httpExchangeRepository() {
        return new InMemoryHttpExchangeRepository();
    }

	@PostConstruct
	public void init() {
        dbInit.signUpTest();
//		dbInit.saveMemberAndVideo();
	}
}