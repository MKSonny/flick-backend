package pro.Flick.repsository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.Flick.entity.Message;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;

    @Test
    void test() {
        List<Message> messages = messageRepository.findByChatRoomId(1L);
        for (Message message : messages) {
            log.info("messages={}", message.getText());
        }
    }
}