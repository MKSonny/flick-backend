package pro.Flick.Video.trash.dto;

import lombok.Data;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Video;

@Data
public class VideoWithMemberDtoV2 {
    private Long id; // 영상 고유 ID
    private String title; // 영상 제목
    private String uri; // 영상 파일 URL
    private Long likeCount; // 좋아요 수
    private Long commentCount; // 댓글 수
    private Long shareCount; // 공유 수
    private GetMemberByIdResponseDto member; // 영상을 올린 사용자 정보

    public VideoWithMemberDtoV2(Video video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.uri = video.getUri();
        this.likeCount = video.getLikesCount();
        this.commentCount = 0L;
        this.shareCount = 0L;
        this.member = new GetMemberByIdResponseDto(video.getMember());
    }
}
