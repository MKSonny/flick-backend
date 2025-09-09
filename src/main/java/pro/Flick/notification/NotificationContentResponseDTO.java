package pro.Flick.notification;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class NotificationContentResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long senderId;
    private Boolean isFollowing;

    public NotificationContentResponseDTO() {
    }

    public NotificationContentResponseDTO(Long id, String content, LocalDateTime createdAt, Long senderId, Boolean isFollowing) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.senderId = senderId;
        this.isFollowing = isFollowing;
    }

    public NotificationContentResponseDTO(Long id, String content, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
    }
}
