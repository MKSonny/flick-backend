package pro.Flick.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.notification.NotificationContentResponseDTO;

import java.util.List;

public interface NotificationRepositoryCustom {

    Page<NotificationContentResponseDTO> QfindMyNotificationsContent(Pageable pageable, Long memberId);

    void QmarkAsReadByIds(List<Long> ids);

    Long QcountUnreadNotifications(Long memberId);
}
