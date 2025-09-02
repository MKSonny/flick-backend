package pro.Flick.chat;

import lombok.Data;
import pro.Flick.entity.ChatRoomMember;

@Data
public class ChatRoomMemberResponseDTO {
    private String username;
    private String profileImageUri;
    private Long chatRoomId;
    private Long memberId;

    public ChatRoomMemberResponseDTO(String username, String profileImageUri, Long chatRoomId, Long memberId) {
        this.username = username;
        this.profileImageUri = profileImageUri;
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
    }

    public ChatRoomMemberResponseDTO(ChatRoomMember chatRoomMember) {
        this.username = chatRoomMember.getMember().getUsername();
        this.profileImageUri =  chatRoomMember.getMember().getProfileImageUri();
        this.chatRoomId = chatRoomMember.getChatRoom().getId();
        this.memberId =chatRoomMember.getMember().getId();
    }
}
