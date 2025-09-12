package pro.Flick.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.chat.ChatRoomMemberResponseDTO;
import pro.Flick.chat.dto.ChatRoomInfoResponseDTO;
import pro.Flick.chat.dto.ChatRoomMemberCountResponseDto;
import pro.Flick.entity.ChatRoom;

public interface ChatRoomMemberRepositoryCustom {

    Page<ChatRoomMemberResponseDTO> QgetMyChats(Pageable pageable, Long memberId);

    Page<ChatRoomMemberResponseDTO> QgetMyChatsV2(Pageable pageable, Long memberId);

    Page<ChatRoomMemberResponseDTO> QgetMyChatsV3(Pageable pageable, Long memberId);

    Page<ChatRoomMemberCountResponseDto> QGetMyChatsCount(Pageable pageable, Long memberId);


    ChatRoomInfoResponseDTO QgetChatRoomInfo(Long chatRoomId, Long memberId);

    ChatRoom QfindChatRoom(Long senderId, Long receiverId);
}
