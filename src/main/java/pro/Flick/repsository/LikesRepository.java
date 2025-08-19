package pro.Flick.repsository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.Flick.entity.Likes;


public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("SELECT count(l) from Likes l where l.video.id = :videoId")
    Long findLikesByVideoId(@Param("videoId") Long videoId);


}
