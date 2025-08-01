package pro.Flick.repsository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.*;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ChatRepository {

    @Autowired
    private EntityManager em;

    /*
        8/1
        채팅방마다 가장 마지막으로 작성한 사람의 이름과 메시지 내용을 전송해야 함

        1. 내가 참여중인 채팅방 목록을 가져와야 함
    */
    public void findChat() {

    }

    // 채팅을 시작할 경우 채팅방이 없다면 -> 채팅방을 새로 만들어야 함
    @Transactional
    public void addMessage(Member sender, Member receiver, String text) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name("test")
                .createdAt(LocalDateTime.now())
                .build();

        Message message = Message.builder()
                .chatRoom(chatRoom)
                .read(false)
                .sender(sender)
                .createdAt(LocalDateTime.now())
                .text(text)
                .build();

        ChatRoomMember addSender = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(sender)
                .build();

        ChatRoomMember addReceiver = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(receiver)
                .build();

        em.persist(chatRoom);
        em.persist(message);
        em.persist(addSender);
        em.persist(addReceiver);
    }

    public List<Chat> getChatByUsersKey(String usersKey) {
        return em.createQuery("select c from Chat c where c.usersKey=:usersKey", Chat.class)
                .setParameter("usersKey", usersKey)
                .getResultList();
    }

    @Transactional
    public void addMessage(Member user_id, String text, String users_key) {
        em.persist(new Chat(users_key, user_id, text, LocalDateTime.now()));
    }
}
