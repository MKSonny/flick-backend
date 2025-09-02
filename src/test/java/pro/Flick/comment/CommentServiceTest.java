package pro.Flick.comment;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pro.Flick.Video.trash.repository.VideoJpaRepository;
import pro.Flick.comment.repository.CommentRepository;
import pro.Flick.comment.trash.dto.GetCommentsByVideoIdResponseDTO;
import pro.Flick.entity.Comment;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.member.repository.MemberRepository;
//import pro.Flick.member.MemberJpaRepository;

import java.util.List;

@SpringBootTest
@Slf4j
class CommentServiceTest {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    VideoJpaRepository videoJpaRepository;

    @Test
    @DisplayName("데이터베이스에 해당 회원과 비디오가 존재하는지 확인")
    void addCommentV1Test() {
        // 여기서 멤버랑 비디오를 찾은 이유?
        // addComment할 때 추가할려고
        Member findMember = memberRepository.findMemberByEmailAndPassword("Email", "123");
        Video findVideo = videoJpaRepository.findVideoById("1");


        log.info("commentService.addCommentV1(findMember.getId(), findVideo.getId(), \"hello\"); start");
        commentService.addCommentV1(findMember.getId(), findVideo.getId(), "hello");
        log.info("commentService.addCommentV1(findMember.getId(), findVideo.getId(), \"hello\"); end");

        List<Comment> comments = commentRepository.findCommentByVideoId(1L);

        List<GetCommentsByVideoIdResponseDTO> collect = comments.stream().map(GetCommentsByVideoIdResponseDTO::new).toList();

        for (GetCommentsByVideoIdResponseDTO c : collect) {
            log.info(c.getUser().getUsername());
            log.info(c.getText());
        }
    }

    @Test
    @DisplayName("데이터베이스에 해당 회원과 비디오가 존재하는지 확인하지 않고 프록시 객체 사용")
    void addCommentV2Test() {
        // 여기서 멤버랑 비디오를 찾은 이유?
        // addComment할 때 추가할려고
        Member findMember = memberRepository.findMemberByEmailAndPassword("Email", "123");
        Video findVideo = videoJpaRepository.findVideoById("1");


        log.info("commentService.addCommentV2(findMember.getId(), findVideo.getId(), \"hello\"); start");
        commentService.addCommentV2(findMember.getId(), findVideo.getId(), "hello");
        log.info("commentService.addCommentV2(findMember.getId(), findVideo.getId(), \"hello\"); end");

        List<Comment> comments = commentRepository.findCommentByVideoId(1L);

        List<GetCommentsByVideoIdResponseDTO> collect = comments.stream().map(GetCommentsByVideoIdResponseDTO::new).toList();

        for (GetCommentsByVideoIdResponseDTO c : collect) {
            log.info(c.getUser().getUsername());
            log.info(c.getText());
        }
    }

    @Test
    void getCommentsLikesInfoTest() {
        Member findMember = memberRepository.findMemberByEmailAndPassword("Email", "123");
        Video findVideo = videoJpaRepository.findVideoById("1");


        log.info("commentService.addCommentV2(findMember.getId(), findVideo.getId(), \"hello\"); start");
        commentService.addCommentV2(findMember.getId(), findVideo.getId(), "hello");
        log.info("commentService.addCommentV2(findMember.getId(), findVideo.getId(), \"hello\"); end");
    }

//    @Test
//    void addCommentAndReply() {
//        Member member = memberRepository.findMemberByEmailAndPassword("Email", "123");
//        Video findVideo = videoJpaRepository.findVideoById("1");
//
//
//        log.info("1");
//        Comment parent = commentService.addCommentV2(member.getId(), findVideo.getId(), "a");
//
//        log.info("2");
//        commentService.addReply(member.getId(), findVideo.getId(), parent.getId(), "b");
//
//        log.info("adfasdfsadf");
//        Pageable pageable = PageRequest.of(0, 3);
//        commentService.findAllCommentsWithLikesInfoV2(pageable, findVideo.getId(), member.getId());
//    }
}