package pro.Flick.notification.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pro.Flick.entity.NotificationType;
import pro.Flick.notification.NotificationContentResponseDTO;
import pro.Flick.notification.NotificationCountResponseDTO;

import java.util.List;

import static pro.Flick.entity.QFollower.follower;
import static pro.Flick.entity.QNotification.notification;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NotificationContentResponseDTO> QfindMyNotificationsContent(NotificationType type, Pageable pageable, Long memberId) {

        List<NotificationContentResponseDTO> content;

        if (NotificationType.FOLLOW.equals(type)) {
            content = queryFactory
                    .select(Projections.constructor(NotificationContentResponseDTO.class,
                            notification.id,
                            notification.content,
                            notification.createdAt,
                            notification.sender.id,
                            follower.id.isNotNull()
                    ))
                    .from(notification)
                    .leftJoin(follower).on(
                            notification.sender.id.eq(follower.following.id)
                                    .and(notification.receiver.id.eq(follower.followed.id))
                    )
                    .where(
                            notification.receiver.id.eq(memberId),
                            notificationTypeEq(type)
                    )
                    .orderBy(notification.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

        } else {
            content = queryFactory
                    .select(Projections.constructor(NotificationContentResponseDTO.class,
                            notification.id,
                            notification.content,
                            notification.createdAt
                    ))
                    .from(notification)
                    .where(
                            notification.receiver.id.eq(memberId),
                            notificationTypeEq(type)
                    )
                    .orderBy(notification.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        Long total = queryFactory
                .select(notification.id.count())
                .from(notification)
                .where(notification.receiver.id.eq(memberId), notificationTypeEq(type))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression notificationTypeEq(NotificationType type) {
        return type != null ? notification.notificationType.eq(type) : null;
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
    public NotificationCountResponseDTO QcountUnreadNotifications(Long memberId) {

        List<Tuple> fetch = queryFactory.select(
                        notification.notificationType,
                        notification.count()
                )
                .from(notification)
                .where(notification.receiver.id.eq(memberId), notification.isRead.eq(false))
                .groupBy(notification.notificationType)
                .fetch();

        NotificationCountResponseDTO dto = new NotificationCountResponseDTO();

        fetch.forEach(tuple -> {
            NotificationType notificationType = tuple.get(notification.notificationType);
            Long count = tuple.get(notification.count());
            if (notificationType == NotificationType.FOLLOW) {
                dto.setFollow_count(count);
            } else if (notificationType == NotificationType.LIKE) {
                dto.setLike_count(count);
            }
        });

        return dto;

//        return queryFactory
//                .select(notification.id.count())
//                .from(notification)
//                .where(notification.receiver.id.eq(memberId), notification.isRead.eq(false))
//                .fetchOne();
    }

    @Override
    public Long QcountAllUnreadNotifications(Long memberId) {
        return queryFactory
                .select(notification.id.count())
                .from(notification)
                .where(notification.receiver.id.eq(memberId), notification.isRead.eq(false))
                .fetchOne();
    }
}
