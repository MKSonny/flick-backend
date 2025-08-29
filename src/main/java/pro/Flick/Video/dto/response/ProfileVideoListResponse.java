package pro.Flick.Video.dto.response;

import lombok.Data;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Video;

@Data
public class ProfileVideoListResponse {
    private Long id;
    private String title;
    private String uri;
    private GetMemberByIdResponseDto member;
    private String thumbnailUri;

    public ProfileVideoListResponse(Video video) {
        this.title = video.getTitle();
        this.uri = video.getUri();
        this.member = new GetMemberByIdResponseDto(video.getMember());

        if (video.getThumbnailStoreFileName() != null) {
            this.thumbnailUri = "/thumbnails/" + video.getThumbnailStoreFileName(); // 썸네일을 제공할 경로
        } else {
            this.thumbnailUri = null; // 또는 기본 이미지 URL
        }
    }
}
