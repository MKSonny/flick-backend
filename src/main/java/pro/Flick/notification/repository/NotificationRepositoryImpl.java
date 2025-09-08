package pro.Flick.notification.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pro.Flick.notification.NotificationContentResponseDTO;

import java.util.List;

import static pro.Flick.entity.QNotification.notification;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NotificationContentResponseDTO> QfindMyNotificationsContent(Pageable pageable, Long memberId) {

        List<NotificationContentResponseDTO> content = queryFactory
                .select(Projections.constructor(NotificationContentResponseDTO.class,
                        notification.id,
                        notification.content
                ))
                .from(notification)
                .where(notification.receiver.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(notification.id.count())
                .from(notification)
                .where(notification.receiver.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public void QmarkAsReadByIds(List<Long> ids) {
        queryFactory
                .update(notification)
                .set(notification.isRead, true)
                .where(notification.id.in(ids), notification.isRead.eq(false))
                .execute();
    }

    @Override
    public Long QcountUnreadNotifications(Long memberId) {
        return queryFactory
                .select(notification.id.count())
                .from(notification)
                .where(notification.receiver.id.eq(memberId), notification.isRead.eq(false))
                .fetchOne();
    }
}
