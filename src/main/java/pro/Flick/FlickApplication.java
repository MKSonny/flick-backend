package pro.Flick;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import pro.Flick.entity.Member;
import pro.Flick.repsository.FileRepository;
import pro.Flick.repsository.MemberRepository;

@SpringBootApplication
@RequiredArgsConstructor
public class FlickApplication {
	private final MemberRepository memberRepository;
	private final FileRepository fileRepository;

	public static void main(String[] args) {
		SpringApplication.run(FlickApplication.class, args);
	}

	@PostConstruct
	public void init() {
		memberRepository.save("HelloWorld", "Email", "123");
		memberRepository.save("test", "Email2", "123");


		Member member = memberRepository.findMember("Email", "123");
		Member member2 = memberRepository.findMember("Email2", "123");
		fileRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
		fileRepository.saveVideo("myVideo2", "http://127.0.0.1:8080/video/test2.mov", member);
		fileRepository.saveVideo("myVideo3", "http://127.0.0.1:8080/video/test3.mov", member2);
	}
}
