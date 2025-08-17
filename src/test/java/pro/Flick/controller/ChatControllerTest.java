package pro.Flick.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import pro.Flick.chat.ChatRoomMemberResponseDTO;
import pro.Flick.entity.ChatRoomMember;
import pro.Flick.entity.Member;
import pro.Flick.entity.Message;
import pro.Flick.repsository.ChatJpaRepository;
import pro.Flick.repsository.ChatRoomMemberRepository;
import pro.Flick.repsository.MemberRepository;

import java.util.List;

@SpringBootTest
@Slf4j
class ChatControllerTest {
    @Autowired
    ChatJpaRepository chatJpaRepository;

    @Autowired
    ChatRoomMemberRepository chatRoomMemberRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DbInit dbInit;

    @BeforeEach
    void add() {
        Member a = Member.builder()
                .username("a")
                .email("a")
                .build();

        Member b = Member.builder()
                .username("b")
                .email("b")
                .build();

        memberRepository.save(a);
        memberRepository.save(b);
    }


    @Test
    @Commit
    void test() {
        Member sender = memberRepository.findMemberByEmail("Email");
        Member receiver = memberRepository.findMemberByEmail("Email2");
        Member a = memberRepository.findMemberByUsernameSingle("a");
        Member b = memberRepository.findMemberByUsernameSingle("b");

        chatJpaRepository.addMessage(sender, receiver, "hello world");
        chatJpaRepository.addMessage(sender, receiver, "hello world1");
        chatJpaRepository.addMessage(sender, receiver, "hello world2");

        chatJpaRepository.addMessage(a, b, "headffasdf");
        chatJpaRepository.addMessage(a, b, "adfsdfa");



        List<Message> temp = chatJpaRepository.temp("HelloWorld");
        for (Message message : temp) {
            log.info("message.getSender().getUsername() {}", message.getSender().getUsername());
            log.info("message.getText() {}", message.getText());
        }
    }

    @Test
    void 내가_참여한_채팅방_정보_목록_불러오기() {

//        Member member = memberRepository.findMemberByEmail("Email");
//        List<ChatRoomMember> chatRoomMembersWithMember = chatRoomMemberRepository.findChatRoomMembersWithMember(member.getId());
//        List<ChatRoomMemberResponseDTO> list = chatRoomMembersWithMember.stream().map(ChatRoomMemberResponseDTO::new).toList();
//        for (ChatRoomMemberResponseDTO chatRoomMemberResponseDTO : list) {
//            log.info("chatRoomMemberResponseDTO={}", chatRoomMemberResponseDTO);
//        }
    }
}