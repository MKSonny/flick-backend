package pro.Flick.controller;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import pro.Flick.entity.Member;
import pro.Flick.entity.Message;
import pro.Flick.repsository.ChatRepository;
import pro.Flick.repsository.MemberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ChatControllerTest {
    @Autowired
    ChatRepository chatRepository;

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
//    @Commit
    void test() {
        Member sender = memberRepository.findMemberByEmail("Email");
        Member receiver = memberRepository.findMemberByEmail("Email2");
        Member a = memberRepository.findMemberByUsernameSingle("a");
        Member b = memberRepository.findMemberByUsernameSingle("b");

        chatRepository.addMessage(sender, receiver, "hello world");
        chatRepository.addMessage(sender, receiver, "hello world1");
        chatRepository.addMessage(sender, receiver, "hello world2");

        chatRepository.addMessage(a, b, "headffasdf");
        chatRepository.addMessage(a, b, "adfsdfa");



        List<Message> temp = chatRepository.temp("HelloWorld");
        for (Message message : temp) {
            log.info("message.getSender().getUsername() {}", message.getSender().getUsername());
            log.info("message.getText() {}", message.getText());
        }

    }
}