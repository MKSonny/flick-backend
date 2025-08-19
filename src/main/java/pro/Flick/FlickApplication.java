package pro.Flick;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pro.Flick.controller.DbInit;

@SpringBootApplication
@RequiredArgsConstructor
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
