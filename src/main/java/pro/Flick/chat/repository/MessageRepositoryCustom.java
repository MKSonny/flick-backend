package pro.Flick.chat.repository;

import java.util.List;

public interface MessageRepositoryCustom {
    void QmarkMessagesAsReadByIds(List<Long> ids);
    void QmarkMessagesAsReadByIdsV2(List<Long> ids, Long memberId);
}
