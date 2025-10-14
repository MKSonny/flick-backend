package pro.Flick.likes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.Video.dto.response.VideoSummaryResponse;
import pro.Flick.member.dto.LikedVideoResponseDTO;

public interface LikesRepositoryCustom {
    Page<LikedVideoResponseDTO> QfindLikedVideosByMemberId(Pageable pageable, Long memberId);
    Page<VideoSummaryResponse> QfindLikedVideosByMemberIdV2(Pageable pageable, Long memberId);
}
