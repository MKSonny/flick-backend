package pro.Flick.Video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.Flick.entity.Video;

import java.util.Collection;
import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("select v from Video v join fetch v.member")
    List<Video> findVideosWithMember();

    @Query("select v from Video v join fetch v.member where v.member.id=:memberId")
    List<Video> findVideosByMemberIdWithMember(@Param("memberId") String memberId);

    @Query("select v from Video v where v.member.id in :memberIds")
    List<Video> findVideoByMemberIds(Collection<Long> memberIds);
}
