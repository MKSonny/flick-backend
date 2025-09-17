package pro.Flick.chat.dto;

import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.MessageStatus;

import java.time.LocalDateTime;

public record GetMessagesResponseDto(
        Long id,
        GetMemberByIdResponseDto user,
        String content,
        LocalDateTime time,
        MessageStatus status
) {
}
