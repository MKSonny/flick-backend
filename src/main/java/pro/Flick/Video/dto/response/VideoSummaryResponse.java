package pro.Flick.Video.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Video;

@NoArgsConstructor
@Getter @Setter
public class VideoSummaryResponse {
    private Long id; // 영상 고유 ID
    private String title; // 영상 제목
    private String uri; // 영상 파일 URL
    private Long likeCount; // 좋아요 수
    private Long commentCount; // 댓글 수
    private Long shareCount; // 공유 수
    private GetMemberByIdResponseDto member; // 영상을 올린 사용자 정보
    private boolean isFollowing;
    private boolean isLikedByUser;
    private String videoType;

    public VideoSummaryResponse(Video video, boolean isFollowing, boolean isLikedByUser) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.uri = video.getUri();
        this.likeCount = video.getLikesCount();
        this.commentCount = video.getCommentCount();
        this.shareCount = 0L;
        this.member = new GetMemberByIdResponseDto(video.getMember());
        this.isFollowing = isFollowing;
        this.isLikedByUser = isLikedByUser;
        this.videoType = video.getVideoType().toString();
    }
}
