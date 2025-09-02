package pro.Flick.Video.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.Video.dto.response.VideoSummaryResponse;
import pro.Flick.entity.Video;

import java.util.List;

public interface VideoRepositoryCustom {
    List<Video> QfindVideosByMemberIdWithMember(Long memberId);
    Page<VideoSummaryResponse> QfindAllVideosV3(Pageable pageable, Long memberId);
    Long QfindTotalLikes(Long memberId);
}
