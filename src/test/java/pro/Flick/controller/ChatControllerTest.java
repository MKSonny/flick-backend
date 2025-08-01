package pro.Flick.controller;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import pro.Flick.entity.Member;
import pro.Flick.repsository.ChatRepository;
import pro.Flick.repsository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatControllerTest {
    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DbInit dbInit;


    @Test
//    @Commit
    void test() {
        Member sender = memberRepository.findMemberByEmail("Email");
        Member receiver = memberRepository.findMemberByEmail("Email2");

        chatRepository.addMessage(sender, receiver, "hello world");
        chatRepository.addMessage(sender, receiver, "hello world1");
        chatRepository.addMessage(sender, receiver, "hello world2");


    }
}