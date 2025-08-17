package pro.Flick.repsository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.Flick.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @EntityGraph(attributePaths = {"sender"})
    List<Message> findByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
