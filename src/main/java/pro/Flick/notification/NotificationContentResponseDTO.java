package pro.Flick.notification;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class NotificationContentResponseDTO {
    private Long id;
    private String content;

    public NotificationContentResponseDTO() {
    }

    public NotificationContentResponseDTO(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
