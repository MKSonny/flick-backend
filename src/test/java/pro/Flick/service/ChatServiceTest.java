package pro.Flick.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.Flick.chat.ChatService;

@SpringBootTest
class ChatServiceTest {

    @Autowired
    ChatService chatService;

    @Test
    void test() {
        chatService.getChatMessages(1L);
    }
}