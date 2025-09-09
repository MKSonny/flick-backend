package pro.Flick.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.chat.ChatRoomMemberResponseDTO;
import pro.Flick.chat.dto.ChatRoomInfoResponseDTO;

public interface ChatRoomMemberRepositoryCustom {

    Page<ChatRoomMemberResponseDTO> QgetMyChats(Pageable pageable, Long memberId);

    Page<ChatRoomMemberResponseDTO> QgetMyChatsV2(Pageable pageable, Long memberId);

    ChatRoomInfoResponseDTO QgetChatRoomInfo(Long chatRoomId, Long memberId);
}
