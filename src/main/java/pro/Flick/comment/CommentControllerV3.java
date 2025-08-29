package pro.Flick.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import pro.Flick.comment.dto.request.CommentAddRequestDTO;
import pro.Flick.comment.dto.response.*;
import pro.Flick.comment.trash.dto.ReplyAddRequestDTO;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("comments")
@RequiredArgsConstructor
public class CommentControllerV3 {

    private final CommentService commentService;

    @GetMapping("/{videoId}")
    public List<VideoCommentResponseDTO> getCommentsByVideoIdV2(@PathVariable Long videoId) {
        return commentService.findCommentByVideoIdV3(videoId);
    }


    @GetMapping("/paging-v2/{videoId}")
    public Page<CommentDetailResponseDTO> getCommentsByVideoIdUsingPaginationV3(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageablee,
                                                                                @PathVariable Long videoId,
                                                                                @RequestParam Long userId) {
        return commentService.getAllCommentsWithLikesInfoV3(pageablee, videoId, userId);
    }


    // 라이브 채팅 목록
    @GetMapping("/paging-v2/live-comments/{videoId}")
    public Page<LiveCommentsResponseDTO> getCommentsByVideoIdUsingPaginationForLive(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageablee,
                                                                                    @PathVariable Long videoId) {
        return commentService.getAllLiveCommentsByVideoId(pageablee, videoId);
    }


    @PostMapping
    public void addCommentV2(@RequestBody CommentAddRequestDTO requestDto) {
        commentService.addCommentV2(requestDto.getUserId(), requestDto.getVideoId(), requestDto.getText());
    }


    @GetMapping("/{parentId}/replies")
    public Page<CommentReplyDetailResponseDTO> getReplies(
            @PathVariable Long parentId,
            @RequestParam Long userId,
            @PageableDefault(size = 3, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) { // 내가 이 답글에 좋아요를 눌렀는지 정보를 가져오기 위해 필요

       return commentService.getRepliesByParentIdV3(pageable, parentId, userId);
    }


    @PostMapping("/{parentId}/reply")
    public CommentReplyDetailResponseDTO replyComment(@PathVariable Long parentId, @RequestBody ReplyAddRequestDTO requestDTO) {
        return commentService.addReplyV3(requestDTO.getUserId(), requestDTO.getVideoId(), parentId, requestDTO.getText());
    }
}
