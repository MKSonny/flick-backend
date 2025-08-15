package pro.Flick.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Member;
import pro.Flick.repsository.ChatRepository;
import pro.Flick.repsository.VideoJpaRepository;
import pro.Flick.repsository.MemberRepository;
import pro.Flick.service.FollowService;

@Service
@RequiredArgsConstructor
public class DbInit {
    private final MemberRepository memberRepository;
    private final VideoJpaRepository videoJpaRepository;
    private final FollowService followService;
    private final ChatRepository chatRepository;

    @Transactional
    public void saveMemberAndVideo() {
        memberRepository.save("HelloWorld", "Email", "123");
        memberRepository.save("test", "Email2", "123");
//        memberRepository.save("test3", "Email2", "123");
//        memberRepository.save("test4", "Email2", "123");


        Member member = memberRepository.findMember("Email", "123");
        Member member2 = memberRepository.findMember("Email2", "123");
        /*
            8/1
            아래와 같이 영상의 제목만 넘겨도 영상을 볼 수 있도록 수정해야 한다
            fileRepository.saveVideo("test")
         */


        videoJpaRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
        videoJpaRepository.saveVideo("myVideo2", "http://127.0.0.1:8080/video/test2.mov", member);
        videoJpaRepository.saveVideo("myVideo3", "http://127.0.0.1:8080/video/test3.mov", member2);

        followService.memberAFollowsMemberB(member, member2);
        followService.memberAFollowsMemberB(member2, member);

//        chatRepository.addMessage(member, "1", "1:2");
//        chatRepository.addMessage(member, "2", "1:2");
//        chatRepository.addMessage(member, "3", "1:2");
//        chatRepository.addMessage(member2, "4", "1:2");
//        chatRepository.addMessage(member2, "5", "1:2");
//        chatRepository.addMessage(member2, "6", "1:2");


        /*
            1. 팔로워 목록에서 누름
            2. 내 member_id, 상대방 member_id 전달
            3. 이렇게 둘이 참여하고 있는 채팅방이 있는지 검사
            4. 없다면 새로운 채팅방을 만든다
         */

        chatRepository.addMessage(member, member2, "1");
        chatRepository.addMessage(member, member2, "2");
        chatRepository.addMessage(member, member2, "3");
        chatRepository.addMessage(member2, member, "4");
        chatRepository.addMessage(member2, member, "5");
        chatRepository.addMessage(member2, member, "6");
    }
}
