package pro.Flick.chat.dto;

import java.util.List;

public record ReadMessagesRequestDto(
        List<Long> messageIds,
        Long userId
) {
}
