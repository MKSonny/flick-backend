package pro.Flick.Video.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import pro.Flick.Video.repository.VideoRepository;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Commit
class VideoServiceTest {

    @Autowired
    VideoService videoService;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Video testVideo;
    private List<Member> testMembers;
    private Member testMember;

    @BeforeEach
    void setUp() {
        // 1. 테스트를 위한 비디오 1개 생성
        Video newVideo = Video.builder().title("Concurrency Test Video").build();
        testVideo = videoRepository.save(newVideo);

        // 2. 테스트를 위한 사용자 100명 생성
        testMembers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Member member = Member.builder()
                    .username("testUser" + i)
                    .email("test" + i + "@email.com")
                    .password("test" + i)
                    .build();
            if (i == 0) {
                testMember = member;
            }
            testMembers.add(member);
        }
        memberRepository.saveAll(testMembers);
    }


    @Test
    @DisplayName("100명의 다른 사용자가 동시에 '좋아요'를 눌러도 likesCount가 정확히 100이 된다")
    void video_addLikes_concurrencyTest_withMultipleUsers() throws InterruptedException {
        // given: 사전 준비
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when: 100개의 스레드가 각자 다른 사용자로 '좋아요'를 동시에 요청
        for (Member member : testMembers) {
            executorService.submit(() -> {
                // 재시도 로직 추가
                while (true) {
                    try {
                        // 각 스레드는 고유한 memberId와 videoId로 서비스를 호출
                        videoService.addLikes(member.getId().toString(), testVideo.getId().toString());
                        break; // 성공 시 루프 탈출
                    } catch (Exception e) {
                        // OptimisticLockException 또는 관련 예외 발생 시
                        // 그냥 넘어가서 재시도
                        System.out.println("Conflict detected for member " + member.getId() + ", retrying...");
                    }
                }
                latch.countDown(); // 작업이 '성공'한 후에만 카운트다운
            });
        }

        latch.await();
        executorService.shutdown();

        // then: 결과 검증
        Video finalVideo = videoRepository.findById(testVideo.getId()).orElseThrow();
        assertEquals(100, finalVideo.getLikesCount(), "100개의 동시 요청 후 '좋아요' 개수는 100이어야 합니다.");
    }

    @Test
    @DisplayName("Likes 제거시 나가는 쿼리 확인")
    void removeLikesTest() {
        log.info("videoService.addLikes(String.valueOf(testMember.getId()), String.valueOf(testVideo.getId()));");
        videoService.addLikes(String.valueOf(testMember.getId()), String.valueOf(testVideo.getId()));

        log.info("videoService.removeLikes(testMember.getId(), testVideo.getId());");
        videoService.removeLikes(testMember.getId(), testVideo.getId());

        assertEquals(0L, testVideo.getLikesCount(), "좋아요를 누르고 취소하면 '좋아요' 개수는 0이어야 합니다.");
    }
}