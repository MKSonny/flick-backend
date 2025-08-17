package pro.Flick.repsository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.Flick.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * select top 1 *
     * from message
     * where chatroom_id = 1
     * order by id desc;
     */
//    @Query("SELECT TOP 1 * FROM Message m WHERE m.chatRoom.id = :chatRoomId ORDER BY m.id desc")
//    List<Message> findMostRecentMessage()
}
