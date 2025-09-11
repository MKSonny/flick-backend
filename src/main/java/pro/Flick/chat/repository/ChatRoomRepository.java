package pro.Flick.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.Flick.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Modifying
    @Query("update ChatRoom cr set cr.lastMessage.id = :messageId where cr.id = :chatRoomId")
    void updateChatRoomMessageId(@Param("messageId") Long messageId, @Param("chatRoomId") Long chatRoomId);
}
