package pro.Flick.comment;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import pro.Flick.comment.dto.request.LiveCommentRequestDTO;
import pro.Flick.comment.dto.response.LiveCommentResponseDTO;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LiveCommentController {

    private final CommentService commentService;

    /**
     * 클라이언트가 /app/chat/{videoId}/sendMessage 경로로 메시지를 보내면 이 메서드가 처리합니다.
     * 처리된 메시지는 /topic/chat/{videoId}를 구독하는 모든 클라이언트에게 전송됩니다.
     * @param videoId 채팅방을 식별하는 비디오 ID
     * @return 브로드캐스팅될 메시지
     */
    @MessageMapping("/comment/{videoId}/sendMessage")
    @SendTo("/topic/comment/{videoId}")
    public LiveCommentResponseDTO sendMessage(
            @DestinationVariable Long videoId,
            LiveCommentRequestDTO messageRequest
    ) {
        // 2. Principal에서 사용자 이름(또는 ID)을 가져옴

        // 3. ChatService에 작업 위임
        // 서비스는 받은 데이터를 가공하고 DB에 저장한 뒤, 완전한 Response DTO를 반환
        log.info("messageRequest={}", messageRequest);
       return commentService.addLiveComment(messageRequest.getSenderId(), messageRequest.getContent(), messageRequest.getVideoId());
    }
}
