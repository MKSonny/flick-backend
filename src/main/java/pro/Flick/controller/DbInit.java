package pro.Flick.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.Video.service.VideoService;
import pro.Flick.entity.Member;
import pro.Flick.repsository.ChatJpaRepository;
import pro.Flick.Video.VideoJpaRepository;
import pro.Flick.repsository.MemberJpaRepository;
import pro.Flick.repsository.MemberRepository;
import pro.Flick.service.ChatService;
import pro.Flick.follow.FollowService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DbInit {
    private final MemberJpaRepository memberJpaRepository;
    private final VideoJpaRepository videoJpaRepository;
    private final FollowService followService;
    private final ChatJpaRepository chatJpaRepository;
    private final VideoService videoService;
    private final ChatService chatService;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveMemberAndVideo() {
        memberJpaRepository.save("HelloWorld", "Email", "123");
        memberJpaRepository.save("test", "Email2", "123");
        memberJpaRepository.save("test3", "Email3", "123");
        memberJpaRepository.save("test4", "Email4", "123");


        Member member = memberJpaRepository.findMember("Email", "123");
        Member member2 = memberJpaRepository.findMember("Email2", "123");
        Member member3 = memberJpaRepository.findMember("Email3", "123");
        Member member4 = memberJpaRepository.findMember("Email4", "123");

        List<Member> memberList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            memberList.add(new Member("test" + i, "sample@email.com" + i, "123"));
        }

        memberRepository.saveAll(memberList);
        /*
            8/1
            아래와 같이 영상의 제목만 넘겨도 영상을 볼 수 있도록 수정해야 한다
            fileRepository.saveVideo("test")
         */



        videoJpaRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
//        videoJpaRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
//        videoJpaRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
//        videoJpaRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
//        videoJpaRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
        videoJpaRepository.saveVideo("myVideo2", "http://127.0.0.1:8080/video/test2.mov", member);
        videoJpaRepository.saveVideo("myVideo3", "http://127.0.0.1:8080/video/test3.mov", member2);

//        videoService.createVideo("test.mov", member);
//        videoService.createVideo("test2.mov", member);
//        videoService.createVideo("test3.mov", member);

        followService.memberAFollowsMemberBUsingRef(member.getId(), member2.getId());
        followService.memberAFollowsMemberBUsingRef(member2.getId(), member.getId());

        for (Member m : memberList) {
//            followService.memberAFollowsMemberB(m, member);
            videoJpaRepository.saveVideo(m.getUsername() + "'s video", "http://127.0.0.1:8080/video/test3.mov", m);
        }

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
}
