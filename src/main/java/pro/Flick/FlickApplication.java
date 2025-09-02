package pro.Flick;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
//import pro.Flick.aop.LogTraceAspect;
import org.springframework.scheduling.annotation.EnableAsync;
import pro.Flick.aop.LogTraceAspect;
import pro.Flick.controller.DbInit;

@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
//@Import(LogTraceAspect.class)
public class FlickApplication {
	private final DbInit dbInit;

	public static void main(String[] args) {
		SpringApplication.run(FlickApplication.class, args);
	}

	@PostConstruct
	public void init() {
		dbInit.saveMemberAndVideo();
	}
}
