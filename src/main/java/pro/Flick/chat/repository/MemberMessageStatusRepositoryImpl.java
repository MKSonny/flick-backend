package pro.Flick.chat.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pro.Flick.chat.dto.ChatRoomMemberCountResponseDto;
import pro.Flick.chat.dto.GetMessagesResponseDto;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.MessageStatus;
import pro.Flick.entity.QChatRoom;
import pro.Flick.entity.QChatRoomMember;
import pro.Flick.entity.QMemberMessageStatus;

import java.util.List;

import static pro.Flick.entity.QChatRoomMember.chatRoomMember;
import static pro.Flick.entity.QMember.member;
import static pro.Flick.entity.QMemberMessageStatus.memberMessageStatus;
import static pro.Flick.entity.QMessage.message;

@RequiredArgsConstructor
public class MemberMessageStatusRepositoryImpl implements MemberMessageStatusRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatRoomMemberCountResponseDto> QGetMyChatsCountV2(Pageable pageable, Long memberId) {
        List<ChatRoomMemberCountResponseDto> content = queryFactory
                .select(Projections.constructor(ChatRoomMemberCountResponseDto.class,
                        memberMessageStatus.chatRoom.id,
                        memberMessageStatus.count()
                ))
                .from(memberMessageStatus)
                .where(memberMessageStatus.chatRoom.id.in(
                        JPAExpressions
                                .select(chatRoomMember.chatRoom.id)
                                .from(chatRoomMember)
                                .where(chatRoomMember.member.id.eq(memberId))
                ), memberMessageStatus.member.id.eq(memberId), memberMessageStatus.status.eq(MessageStatus.DELIVERED))
                .groupBy(memberMessageStatus.chatRoom.id)
                .fetch();


        Long total = queryFactory
                .select(chatRoomMember.count())
                .from(chatRoomMember)
                .where(chatRoomMember.member.id.eq(memberId))
                .fetchOne();


        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<GetMessagesResponseDto> QfindMessages(Pageable pageable, Long chatRoomId, Long memberId) {
        List<GetMessagesResponseDto> content = queryFactory
                .select(Projections.constructor(GetMessagesResponseDto.class,
                        message.id,
                        Projections.constructor(GetMemberByIdResponseDto.class,
                                message.sender),
                        message.text,
                        message.createdAt,
                        memberMessageStatus.status
                ))
                .from(memberMessageStatus)
                .join(memberMessageStatus.message, message)
                .where(
                        message.chatRoom.id.eq(chatRoomId),
                        memberMessageStatus.member.id.eq(memberId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(message.createdAt.asc())
                .fetch();


        Long total = queryFactory
                .select(message.count())
                .from(message)
                .where(message.chatRoom.id.eq(chatRoomId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
