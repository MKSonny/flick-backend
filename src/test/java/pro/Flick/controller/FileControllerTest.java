package pro.Flick.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.repsository.FileRepository;
import pro.Flick.repsository.MemberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class FileControllerTest {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void init() {
        Member member = Member.builder()
                .username("hello world")
                .password("123")
                .email("email")
                .build();
        Member savedMember = memberRepository.save(member);

        Video video = Video.builder()
                .uri("test.mp4")
                .title("test video")
                .member(savedMember)
                .build();

        fileRepository.saveVideo(video);
    }

    @Test
    public void getAllVideosTest() {
        List<Video> videos = fileRepository.getAllVideos();
        for (Video video : videos) {
            video.getMember();
        }
    }
}