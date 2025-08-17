package pro.Flick.repsository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.Flick.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
