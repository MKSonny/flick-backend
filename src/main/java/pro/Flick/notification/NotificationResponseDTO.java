package pro.Flick.notification;

import lombok.Builder;
import lombok.Getter;
import pro.Flick.entity.Notification;

@Getter
public class NotificationResponseDTO {

    private final Long id;
    private final String content;
    private final Boolean isRead;
    private final String notificationType;

    @Builder
    public NotificationResponseDTO(Long id, String content, Boolean isRead, String notificationType) {
        this.id = id;
        this.content = content;
        this.isRead = isRead;
        this.notificationType = notificationType;
    }

    public static NotificationResponseDTO from(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .isRead(notification.getIsRead())
                .notificationType(notification.getNotificationType().name())
                .build();
    }
}
