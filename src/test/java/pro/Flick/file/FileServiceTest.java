package pro.Flick.file;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.Flick.entity.Member;
import pro.Flick.member.repository.MemberRepository;

@SpringBootTest
class FileServiceTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FileService fileService;

    @Test
    void getFileImageUriTest() {
        Member member = memberRepository.findByEmail("email");
//
//        fileService.storeImage()
    }
}