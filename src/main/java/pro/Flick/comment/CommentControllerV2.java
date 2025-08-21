package pro.Flick.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentControllerV2 {
    private final CommentService commentService;

    @GetMapping("/{videoId}")
    public List<GetCommentsByVideoIdResponseDTO> getCommentsByVideoIdV2(@PathVariable Long videoId) {
        return commentService.findCommentByVideoId(videoId);
    }

    @GetMapping("/paging/{videoId}")
    public Page<GetCommentsByVideoIdWithLikesInfoResponseDTO> getCommentsByVideoIdUsingPagination(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageablee,
                                                                                                  @PathVariable Long videoId,
                                                                                                  @RequestParam Long userId) {
        return commentService.findAllCommentsWithLikesInfo(pageablee, videoId, userId);
    }

    @PostMapping
    public void addCommentV2(@RequestBody CommentAddRequestDTO requestDto) {
        commentService.addCommentV2(Long.valueOf(requestDto.getUserId()), Long.valueOf(requestDto.getVideoId()), requestDto.getText());
    }
}
