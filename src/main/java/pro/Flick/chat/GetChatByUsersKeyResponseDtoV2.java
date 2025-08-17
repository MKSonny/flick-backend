package pro.Flick.chat;

import lombok.Data;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Message;

import java.time.LocalDateTime;

@Data
public class GetChatByUsersKeyResponseDtoV2 {
    private GetMemberByIdResponseDto user;
    private String text;
    private LocalDateTime time;

    public GetChatByUsersKeyResponseDtoV2(Message message) {
        this.user = new GetMemberByIdResponseDto(message.getSender());
        this.text = message.getText();
        this.time = message.getCreatedAt();
    }
}