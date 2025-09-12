package pro.Flick.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.chat.dto.ChatRoomMemberCountResponseDto;

public interface MemberMessageStatusRepositoryCustom {
    Page<ChatRoomMemberCountResponseDto> QGetMyChatsCountV2(Pageable pageable, Long memberId);

}
