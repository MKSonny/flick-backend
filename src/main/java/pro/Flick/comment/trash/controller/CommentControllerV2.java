//package pro.Flick.comment;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.web.bind.annotation.*;
//import pro.Flick.comment.dto.request.CommentAddRequestDTO;
//import pro.Flick.comment.dto.response.LiveCommentsResponseDTO;
//
//import java.util.List;
//
//@Slf4j
//@RestController
//@RequestMapping("/comments")
//@RequiredArgsConstructor
//public class CommentControllerV2 {
//    private final CommentService commentService;
//
//    @GetMapping("/{videoId}")
//    public List<GetCommentsByVideoIdResponseDTO> getCommentsByVideoIdV2(@PathVariable Long videoId) {
//        return commentService.findCommentByVideoId(videoId);
//    }
//
//    @GetMapping("/paging/{videoId}")
//    public Page<GetCommentsByVideoIdWithLikesInfoResponseDTO> getCommentsByVideoIdUsingPagination(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageablee,
//                                                                                                  @PathVariable Long videoId,
//                                                                                                  @RequestParam Long userId) {
//        return commentService.findAllCommentsWithLikesInfo(pageablee, videoId, userId);
//    }
//
//    @GetMapping("/paging-v2/{videoId}")
//    public Page<GetCommentsByVideoIdWithLikesInfoResponseDTOV2> getCommentsByVideoIdUsingPaginationV2(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageablee,
//                                                                                                  @PathVariable Long videoId,
//                                                                                                  @RequestParam Long userId) {
//        return commentService.findAllCommentsWithLikesInfoV2(pageablee, videoId, userId);
//    }
//
//    // 라이브 채팅 목록
//    @GetMapping("/paging-v2/live-comments/{videoId}")
//    public Page<LiveCommentsResponseDTO> getCommentsByVideoIdUsingPaginationForLive(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageablee,
//                                                                                       @PathVariable Long videoId) {
//        return commentService.getAllLiveCommentsByVideoId(pageablee, videoId);
//    }
//
//    @PostMapping
//    public void addCommentV2(@RequestBody CommentAddRequestDTO requestDto) {
//        commentService.addCommentV2(Long.valueOf(requestDto.getUserId()), Long.valueOf(requestDto.getVideoId()), requestDto.getText());
//    }
//
//    @GetMapping("/{parentId}/replies")
//    public Page<GetReplysByParentIdWithLikesInfoResponseDTO> getReplies(
//            @PathVariable Long parentId,
//            @RequestParam Long userId,
//            @PageableDefault(size = 3, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) { // 내가 이 답글에 좋아요를 눌렀는지 정보를 가져오기 위해 필요
//
//       return commentService.getRepliesByParentId(pageable, parentId, userId);
//    }
//
//    /*
//        userId: user.id,
//        videoId: params.video_id,
//        parentId: parentId,
//        text: text,
//     */
//    @PostMapping("/{parentId}/reply")
//    public GetReplysByParentIdWithLikesInfoResponseDTO replyComment(@PathVariable Long parentId, @RequestBody ReplyAddRequestDTO requestDTO) {
//        return commentService.addReply(requestDTO.getUserId(), requestDTO.getVideoId(), parentId, requestDTO.getText());
//    }
//}
