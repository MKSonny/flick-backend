package pro.Flick.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pro.Flick.api_response.ApiResponse;
import pro.Flick.comment.dto.request.CommentAddRequestDTO;
import pro.Flick.comment.dto.response.*;
import pro.Flick.comment.trash.dto.ReplyAddRequestDTO;
import pro.Flick.entity.NotificationType;
import pro.Flick.member.CustomUserDetails;
import pro.Flick.notification.NotificationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("comments")
@RequiredArgsConstructor
public class CommentControllerV3 {

    private final CommentService commentService;
    private final NotificationService notificationService;

    @GetMapping("/{videoId}")
    public ApiResponse<List<VideoCommentResponseDTO>> getCommentsByVideoIdV2(@PathVariable Long videoId) {
//        return commentService.findCommentByVideoIdV3(videoId);
        return ApiResponse.ok(commentService.findCommentByVideoIdV3UsingQueryDsl(videoId));
    }


    @GetMapping("/paging-v2/{videoId}")
    public ApiResponse<Page<CommentDetailResponseDTO>> getCommentsByVideoIdUsingPaginationV3(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageablee,
                                                                                @PathVariable Long videoId,
                                                                                @AuthenticationPrincipal CustomUserDetails user) {
//        return commentService.getAllCommentsWithLikesInfoV3(pageablee, videoId, userId);
        return ApiResponse.ok(commentService.getAllCommentsWithLikesInfoUsingQueryDsl(pageablee, videoId, user.getId()));
    }


    // 라이브 채팅 목록
    @GetMapping("/paging-v2/live-comments/{videoId}")
    public Page<LiveCommentsResponseDTO> getCommentsByVideoIdUsingPaginationForLive(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageablee,
                                                                                    @PathVariable Long videoId) {
//        return commentService.getAllLiveCommentsByVideoId(pageablee, videoId);
        return commentService.getAllLiveCommentsByVideoIdUsingQueryDsl(pageablee, videoId);
    }


    @PostMapping
    public ApiResponse<CommentDetailResponseDTO> addCommentV2(@AuthenticationPrincipal CustomUserDetails user, @RequestBody CommentAddRequestDTO requestDto) {
        notificationService.send(requestDto.getVideo_user_id(), user.getId(), NotificationType.COMMENT, requestDto.getText());
        return ApiResponse.ok(commentService.addCommentV2(user.getId(), requestDto.getVideoId(), requestDto.getText()));
    }


    @GetMapping("/{parentId}/replies")
    public ApiResponse<Page<CommentReplyDetailResponseDTO>> getReplies(
            @PathVariable Long parentId,
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 3, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) { // 내가 이 답글에 좋아요를 눌렀는지 정보를 가져오기 위해 필요

//       return commentService.getRepliesByParentIdV3(pageable, parentId, userId);
        return ApiResponse.ok(commentService.getRepliesByParentIdV3UsingQueryDsl(pageable, parentId, user.getId()));
    }


    @PostMapping("/{parentId}/reply")
    public ApiResponse<CommentReplyDetailResponseDTO> replyComment(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long parentId, @RequestBody ReplyAddRequestDTO requestDTO) {
        return ApiResponse.ok(commentService.addReplyV3(user.getId(), requestDTO.getVideoId(), parentId, requestDTO.getText()));
    }
}
