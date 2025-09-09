package pro.Flick.notification;
import lombok.Data;

@Data
public class NotificationCountResponseDTO {
    private Long follow_count = 0L;
    private Long like_count = 0L;

    public NotificationCountResponseDTO(Long follow_count, Long like_count) {
        this.follow_count = follow_count;
        this.like_count = like_count;
    }

    public NotificationCountResponseDTO() {
    }
}
