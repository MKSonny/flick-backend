package pro.Flick.repsository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.Flick.chat.repository.ChatRoomMemberRepository;
import pro.Flick.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
@RequiredArgsConstructor
class ChatRoomMemberJpaRepositoryTest {

    @Autowired
    ChatRoomMemberRepository chatRoomMemberRepository;

    @Test
    void test() {
//        List<ChatRoomMember> chatRoomMembersWithMember = chatRoomMemberRepository.findChatRoomMembersWithMember(1L);
//        for (ChatRoomMember chatRoomMember : chatRoomMembersWithMember) {
//            System.out.println("chatRoomMember.getFollowed().getUsername() = " + chatRoomMember.getFollowed().getUsername());
//        }

    }

    @Test
    void test2() {
        Optional<ChatRoom> chatRoomByMemberIds = chatRoomMemberRepository.findChatRoomByMemberIds(List.of(1L, 2L), 2);
    }
}