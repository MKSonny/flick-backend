package pro.Flick.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LiveCommentRequestDTO {
    private Long senderId; // 메시지 내용만 받음
    private String content;
    private Long videoId;
}