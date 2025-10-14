package pro.Flick.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikedVideoResponseDTO {
    private Long id;
    private String thumbnailUri; // 추가된 필드

    public LikedVideoResponseDTO(Long id, String thumbnailUri) {
        this.id = id;
        if (thumbnailUri != null) {
            this.thumbnailUri = thumbnailUri;
        } else {
            this.thumbnailUri = thumbnailUri;
        }
    }
}
