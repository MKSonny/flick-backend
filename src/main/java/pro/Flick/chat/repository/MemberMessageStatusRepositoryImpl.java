package pro.Flick.chat.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pro.Flick.chat.dto.ChatRoomMemberCountResponseDto;
import pro.Flick.entity.MessageStatus;
import pro.Flick.entity.QChatRoom;
import pro.Flick.entity.QChatRoomMember;
import pro.Flick.entity.QMemberMessageStatus;

import java.util.List;

import static pro.Flick.entity.QChatRoomMember.chatRoomMember;
import static pro.Flick.entity.QMemberMessageStatus.memberMessageStatus;

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
}
