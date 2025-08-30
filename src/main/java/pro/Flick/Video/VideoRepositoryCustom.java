package pro.Flick.Video;

import pro.Flick.entity.Video;

import java.util.List;

public interface VideoRepositoryCustom {
    List<Video> QfindVideosByMemberIdWithMember(Long memberId);
}
