package pro.Flick.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.Video.VideoRepository;
import pro.Flick.Video.service.VideoService;
import pro.Flick.comment.CommentService;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.entity.VideoType;
import pro.Flick.repsository.ChatJpaRepository;
import pro.Flick.Video.trash.repository.VideoJpaRepository;
import pro.Flick.member.MemberRepository;
import pro.Flick.service.ChatService;
import pro.Flick.follow.FollowService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DbInit {
    private final MemberRepository memberRepository;
    private final VideoJpaRepository videoJpaRepository;
    private final FollowService followService;
    private final ChatJpaRepository chatJpaRepository;
    private final VideoService videoService;
    private final ChatService chatService;
    private final CommentService commentService;
    private final VideoRepository videoRepository;

    @Transactional
    public void saveMemberAndVideo() {

        memberRepository.save(new Member("HelloWorld", "Email", "123"));
        memberRepository.save(new Member("test", "Email2", "123"));
        memberRepository.save(new Member("test3", "Email3", "123"));
        memberRepository.save(new Member("test4", "Email4", "123"));


        Member member = memberRepository.findMemberByEmailAndPassword("Email", "123");
        Member member2 = memberRepository.findMemberByEmailAndPassword("Email2", "123");
        Member member3 = memberRepository.findMemberByEmailAndPassword("Email3", "123");
        Member member4 = memberRepository.findMemberByEmailAndPassword("Email4", "123");

        List<Member> memberList = new ArrayList<>();

        save1000TestMembers(memberList);

        for (Member m : memberList) {
            followService.memberAFollowsMemberB(member, m);
        }

        memberRepository.saveAll(memberList);


        /*
            8/1
            아래와 같이 영상의 제목만 넘겨도 영상을 볼 수 있도록 수정해야 한다
            fileRepository.saveVideo("test")
         */


        Video AdVideoTest = Video.builder()
                .videoType(VideoType.AD)
                .uri("http://127.0.0.1:8080/videos-v2/download/test.mov")
                .title("광고 영상입니다.")
                .member(member)
                .build();

        Video ShoppingVideoTest = Video.builder()
                .videoType(VideoType.SHOPPING)
                .uri("http://127.0.0.1:8080/videos-v2/download/test.mov")
                .title("쇼핑 영상입니다.")
                .member(member)
                .build();

        videoRepository.save(AdVideoTest);
        videoRepository.save(ShoppingVideoTest);
        videoJpaRepository.saveVideo("myVideo", "http://127.0.0.1:8080/videos-v2/download/test.mov", member);
//        videoJpaRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
//        videoJpaRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
//        videoJpaRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
//        videoJpaRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);

        videoJpaRepository.saveVideo("myVideo2", "http://127.0.0.1:8080/videos-v2/download/test2.mov", member);
        videoJpaRepository.saveVideo("myVideo3", "http://127.0.0.1:8080/videos-v2/download/test3.mov", member2);

//        videoService.createVideo("test.mov", member);
//        videoService.createVideo("test2.mov", member);
//        videoService.createVideo("test3.mov", member);

        followService.memberAFollowsMemberBUsingRef(member.getId(), member2.getId());
        followService.memberAFollowsMemberBUsingRef(member2.getId(), member.getId());


        commentService.addCommentV2(1L, 1L, "hello world1");
        commentService.addCommentV2(1L, 1L, "hello world2");
        commentService.addCommentV2(1L, 1L, "hello world3");
        commentService.addCommentV2(1L, 1L, "hello world4");
        commentService.addCommentV2(2L, 1L, "hello world5");

//        save100MembersVideos(memberList);

//        chatJpaRepository.addMessage(member, "1", "1:2");
//        chatJpaRepository.addMessage(member, "2", "1:2");
//        chatJpaRepository.addMessage(member, "3", "1:2");
//        chatJpaRepository.addMessage(member2, "4", "1:2");
//        chatJpaRepository.addMessage(member2, "5", "1:2");
//        chatJpaRepository.addMessage(member2, "6", "1:2");


        /*
            1. 팔로워 목록에서 누름
            2. 내 member_id, 상대방 member_id 전달
            3. 이렇게 둘이 참여하고 있는 채팅방이 있는지 검사
            4. 없다면 새로운 채팅방을 만든다
         */

        chatService.addMessage(member, member2, "1");
        chatService.addMessage(member, member2, "2");
        chatService.addMessage(member, member2, "3");
        chatService.addMessage(member2, member, "4");
        chatService.addMessage(member2, member, "5");
        chatService.addMessage(member2, member, "6");

        chatService.addMessage(member, member3, "6");
        chatService.addMessage(member, member3, "6");
        chatService.addMessage(member, member3, "6");
    }

    private void save100MembersVideos(List<Member> memberList) {
        for (Member m : memberList) {
            videoJpaRepository.saveVideo(m.getUsername() + "'s video", "http://127.0.0.1:8080/video/test3.mov", m);
        }
    }

    private static void save1000TestMembers(List<Member> memberList) {
        for (int i = 0; i < 100; i++) {
            memberList.add(new Member("user" + i, "sample@email.com" + i, "123"));
        }
    }
}
