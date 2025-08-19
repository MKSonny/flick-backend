package pro.Flick.repsository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.Flick.Video.VideoJpaRepository;
import pro.Flick.comment.CommentService;
import pro.Flick.comment.GetCommentsByMemberIdResponseDTO;
import pro.Flick.entity.Comment;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;

import java.util.List;

@Slf4j
@SpringBootTest
class CommentRepositoryTest {

}