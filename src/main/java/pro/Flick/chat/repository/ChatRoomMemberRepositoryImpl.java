package pro.Flick.chat.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pro.Flick.chat.ChatRoomMemberResponseDTO;
import pro.Flick.entity.*;

import java.util.List;

import static pro.Flick.entity.QChatRoomMember.chatRoomMember;
import static pro.Flick.entity.QFile.file;
import static pro.Flick.entity.QMember.member;
import static pro.Flick.entity.QMessage.message;


@RequiredArgsConstructor
public class ChatRoomMemberRepositoryImpl implements ChatRoomMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatRoomMemberResponseDTO> QgetMyChats(Pageable pageable, Long memberId) {
        QChatRoomMember crm1 = new QChatRoomMember("crm1");
        QChatRoomMember crm2 = new QChatRoomMember("crm2");

        List<ChatRoomMemberResponseDTO> content = queryFactory
                .select(Projections.constructor(
                        ChatRoomMemberResponseDTO.class,
                        member.username,
                        file.storedFileName,
                        crm2.chatRoom.id,
                        member.id
                ))
                .from(crm1)
                .join(crm2).on(crm1.chatRoom.id.eq(crm2.chatRoom.id))
                .join(crm2.member, member)
                .leftJoin(member.file, file)
                .where(
                        crm1.member.id.eq(memberId),
                        crm1.member.id.ne(crm2.member.id)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(crm2.count())
                .from(crm1)
                .join(crm2).on(crm1.chatRoom.id.eq(crm2.chatRoom.id))
                .where(
                        crm1.member.id.eq(memberId),
                        crm1.member.id.ne(crm2.member.id)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ChatRoomMemberResponseDTO> QgetMyChatsV2(Pageable pageable, Long memberId) {

        QChatRoomMember crm1 = new QChatRoomMember("crm1");
        QChatRoomMember crm2 = new QChatRoomMember("crm2");

        List<Long> chatRoomIds = queryFactory
                .select(crm2.chatRoom.id)
                .from(crm1)
                .join(crm2).on(crm1.chatRoom.id.eq(crm2.chatRoom.id))
                .where(crm1.member.id.eq(memberId), crm1.member.id.ne(crm2.member.id))
                .fetch();


        QMessage m2 = new QMessage("m2");

        List<ChatRoomMemberResponseDTO> content = queryFactory
                .select(Projections.constructor(ChatRoomMemberResponseDTO.class,
                        message.sender.username,
                        message.sender.file.storedFileName,
                        message.chatRoom.id,
                        message.sender.id,
                        message.text
                ))
                .from(message)
                .join(message.sender, member)
                .leftJoin(message.sender.file, file)
                .where(message.chatRoom.id.in(chatRoomIds), message.createdAt.eq(
                        JPAExpressions
                                .select(m2.createdAt.max())
                                .from(m2)
                                .where(m2.chatRoom.id.eq(message.chatRoom.id))
                ))
                .orderBy(message.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(message.count())
                .from(message)
                .where(message.chatRoom.id.in(chatRoomIds), message.createdAt.eq(
                        JPAExpressions
                                .select(m2.createdAt.max())
                                .from(m2)
                                .where(m2.chatRoom.id.eq(message.chatRoom.id))
                ))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
