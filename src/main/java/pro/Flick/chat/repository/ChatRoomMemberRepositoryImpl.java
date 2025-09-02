package pro.Flick.chat.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pro.Flick.chat.ChatRoomMemberResponseDTO;
import pro.Flick.entity.ChatRoomMember;
import pro.Flick.entity.QChatRoomMember;
import pro.Flick.entity.QFile;
import pro.Flick.entity.QMember;

import java.util.List;

import static pro.Flick.entity.QChatRoomMember.chatRoomMember;
import static pro.Flick.entity.QFile.file;
import static pro.Flick.entity.QMember.member;


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
}
