package pro.Flick.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pro.Flick.entity.MessageStatus;
import pro.Flick.entity.QMemberMessageStatus;
import pro.Flick.entity.QMessage;

import java.util.List;

import static pro.Flick.entity.QMemberMessageStatus.memberMessageStatus;
import static pro.Flick.entity.QMessage.message;

@Slf4j
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public void QmarkMessagesAsReadByIds(List<Long> ids) {

        queryFactory
                .update(message)
                .set(message.read, true)
                .where(message.id.in(ids), message.read.eq(false))
                .execute();
    }

    @Override
    public void QmarkMessagesAsReadByIdsV2(List<Long> ids, Long memberId) {

        log.info("QmarkMessagesAsReadByIdsV2={}", ids);

        queryFactory
                .update(memberMessageStatus)
                .set(memberMessageStatus.status, MessageStatus.READ)
                .where(memberMessageStatus.status.eq(MessageStatus.DELIVERED), memberMessageStatus.message.id.in(ids), memberMessageStatus.member.id.eq(memberId))
                .execute();
    }
}
