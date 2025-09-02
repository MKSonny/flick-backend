package pro.Flick.chat;

import lombok.Data;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Message;

import java.time.LocalDateTime;

@Data
public class GetChatByUsersKeyResponseDtoV2 {
    private Long id;
    private GetMemberByIdResponseDto user;
    private String content;
    private LocalDateTime time;

    public GetChatByUsersKeyResponseDtoV2(Message message) {
        this.id = message.getId();
        this.user = new GetMemberByIdResponseDto(message.getSender());
        this.content = message.getText();
        this.time = message.getCreatedAt();
    }
}