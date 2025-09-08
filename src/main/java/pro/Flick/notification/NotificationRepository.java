package pro.Flick.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.Flick.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
