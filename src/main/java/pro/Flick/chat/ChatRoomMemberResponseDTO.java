package pro.Flick.chat;

import lombok.Data;
import pro.Flick.entity.ChatRoomMember;

@Data
public class ChatRoomMemberResponseDTO {
    private String username;
    private String profileImageUri;
    private Long chatRoomId;
    private Long memberId;


    public ChatRoomMemberResponseDTO(ChatRoomMember chatRoomMember) {
        this.username = chatRoomMember.getMember().getUsername();
        this.profileImageUri =  chatRoomMember.getMember().getProfileImageUri();
        this.chatRoomId = chatRoomMember.getChatRoom().getId();
        this.memberId =chatRoomMember.getMember().getId();
    }
}
