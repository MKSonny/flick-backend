package pro.Flick.controller;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.member.MemberJpaRepository;
import pro.Flick.Video.VideoJpaRepository;

import java.util.List;


@SpringBootTest
@Transactional
@Slf4j
class VideoControllerTest {
    @Autowired
    private VideoJpaRepository fileRepository;
    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void init() {
        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .username("hello world" + i)
                    .password("123")
                    .email("email")
                    .build();
            Member savedMember = memberJpaRepository.save(member);

            Video video = Video.builder()
                    .uri("test.mp4")
                    .title("test video" + i)
                    .member(savedMember)
                    .build();

            fileRepository.saveVideo(video);
        }
    }

    @Test
    public void getAllVideosTest() {
        em.flush();
        // N+1 문제 검증을 위해 1차 캐시를 비운다.
        em.clear();

        log.info("--- 비디오 조회 시작 ---");
        List<Video> videos = fileRepository.getAllVideos(); // 이 시점에 비디오 10개 조회 (1번 쿼리)
        log.info("--- 비디오 조회 완료 ---");

        for (Video video : videos) {
            // 지연 로딩으로 인해 member 정보가 필요할 때마다 쿼리가 실행
            log.info("video:{} member:{}", video.getTitle(), video.getMember().getUsername());
        }
    }

//    @Test
//    public void joinFetchTest() {
//        em.flush();
//        em.clear();
//
//        log.info("--- 비디오 조회 시작 ---");
////        List<Video> videos = fileRepository.fetchJoinFindVideos();
//        log.info("--- 비디오 조회 완료 ---");
//
//        for (Video video : videos) {
//            log.info("video:{} member:{}", video.getTitle(), video.getMember().getUsername());
//        }
//    }
}