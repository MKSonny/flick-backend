package pro.Flick.notification.repository;

import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.entity.NotificationType;
import pro.Flick.notification.NotificationContentResponseDTO;
import pro.Flick.notification.NotificationCountResponseDTO;

import java.util.List;

public interface NotificationRepositoryCustom {

    Page<NotificationContentResponseDTO> QfindMyNotificationsContent(NotificationType type, Pageable pageable, Long memberId);

    void QmarkAsReadByIds(List<Long> ids);

    NotificationCountResponseDTO QcountUnreadNotifications(Long memberId);

    Long QcountAllUnreadNotifications(Long memberId);
}
