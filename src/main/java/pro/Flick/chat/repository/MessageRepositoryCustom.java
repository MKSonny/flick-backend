package pro.Flick.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.chat.dto.GetMessagesResponseDto;

import java.util.List;

public interface MessageRepositoryCustom {
    void QmarkMessagesAsReadByIds(List<Long> ids);
    void QmarkMessagesAsReadByIdsV2(List<Long> ids, Long memberId);
    Page<GetMessagesResponseDto> QfindMessagesV2(Pageable pageable, Long chatRoomId, Long memberId);
}
