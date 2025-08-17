package pro.Flick.repsository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.*;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ChatJpaRepository {

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

//        ChatRoomMember chatRoomMember = findChatBySenderMemberIdReceiverMemberId(sender.getId(), receiver.getId());
        ChatRoom chatRoom = findChatRoomBySenderMemberIdReceiverMemberId(sender.getId(), receiver.getId());
//
        if (chatRoom == null) {
            chatRoom = ChatRoom.builder()
                    .name("test")
                    .createdAt(LocalDateTime.now())
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
            em.persist(addSender);
            em.persist(addReceiver);
        }

        Message message = Message.builder()
                .chatRoom(chatRoom)
                .read(false)
                .sender(sender)
                .createdAt(LocalDateTime.now())
                .text(text)
                .build();



//        em.persist(chatRoom);
        em.persist(message);
//        em.persist(addSender);
//        em.persist(addReceiver);
    }

    // sender와 receiver가 참여하고 있는 채팅방이 있는지 검사
    public ChatRoom findChatRoomBySenderMemberIdReceiverMemberId(Long senderId, Long receiverId) {
        List<ChatRoom> resultList = em.createQuery(
                        "SELECT cr " +
                                "FROM ChatRoom cr " +
                                "WHERE cr.id IN (" +
                                "    SELECT crm.chatRoom.id " +
                                "    FROM ChatRoomMember crm " +
                                "    WHERE crm.member.id IN (:senderId, :receiverId) " +
                                "    GROUP BY crm.chatRoom.id " +
                                "    HAVING count(crm.chatRoom.id) = 2" +
                                ")", ChatRoom.class)
                .setParameter("senderId", senderId)
                .setParameter("receiverId", receiverId)
                .getResultList();

        return resultList.isEmpty() ? null : resultList.get(0);
    }


    // 내가 참여하고 있는 채팅방 목록 확인
    public List<ChatRoom> findChatRoomMemberByMemberId(String memberId) {
        return em.createQuery("select c from ChatRoomMember c where c.member.id=:memberId", ChatRoom.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }


    // 내가 참여하고 있는 채팅방들을 가져옴 -> 그 채팅방의 마지막 text를 가져옴(Message.chatRoom)
    public List<Message> temp(String userName) {
        return em.createQuery("select m from Message m where m.chatRoom.id in " +
                        "(select crm.id from ChatRoomMember crm where crm.member.username=:userName)", Message.class)
                .setParameter("userName", userName)
                .getResultList();

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
