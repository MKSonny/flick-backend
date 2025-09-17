package pro.Flick.chat.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pro.Flick.chat.dto.GetMessagesResponseDto;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.MessageStatus;
import pro.Flick.entity.QMember;
import pro.Flick.entity.QMemberMessageStatus;
import pro.Flick.entity.QMessage;

import java.util.List;

import static pro.Flick.entity.QMember.member;
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

        log.info("QmarkMessagesAsReadByIdsV2={}, memberId={}", ids, memberId);

        queryFactory
                .update(memberMessageStatus)
                .set(memberMessageStatus.status, MessageStatus.READ)
                .where(memberMessageStatus.status.eq(MessageStatus.DELIVERED), memberMessageStatus.message.id.in(ids), memberMessageStatus.member.id.eq(memberId))
                .execute();
    }

    @Override
    public Page<GetMessagesResponseDto> QfindMessagesV2(Pageable pageable, Long chatRoomId, Long memberId) {
        List<GetMessagesResponseDto> content = queryFactory
                .select(Projections.constructor(GetMessagesResponseDto.class,
                        message.id,
                        Projections.constructor(GetMemberByIdResponseDto.class,
                                member),
                        message.text,
                        message.createdAt,
                        memberMessageStatus.status
                ))
                .from(message)
                .join(memberMessageStatus.message, message).on(memberMessageStatus.message.id.eq(message.id))
                .join(message.sender, member)
                .where(message.chatRoom.id.eq(chatRoomId), memberMessageStatus.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(message.count())
                .from(message)
                .where(message.chatRoom.id.eq(chatRoomId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }


}
