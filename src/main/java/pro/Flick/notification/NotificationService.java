package pro.Flick.notification;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pro.Flick.entity.Member;
import pro.Flick.entity.Notification;
import pro.Flick.entity.NotificationType;
import pro.Flick.follow.FollowService;
import pro.Flick.member.repository.MemberRepository;
import pro.Flick.notification.repository.EmitterRepository;
import pro.Flick.notification.repository.NotificationRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final FollowService followService;

    private static  final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간

    public SseEmitter subscribe(Long memberId) {

        String emitterId = memberId + "_"  + System.currentTimeMillis();

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError((e) -> emitterRepository.deleteById(emitterId));


        String message = "Connection established for user " + memberId;
        Map<String, String> connectionData = Map.of("message", message);

        sendToClient("sse", emitter, emitterId, connectionData);

        return emitter;
    }


    @Transactional
    public void send(Long receiverId, Long senderId, NotificationType notificationType, String comment) {
        Member sender = memberRepository.findById(senderId).orElseThrow();
        Member receiver = memberRepository.findById(receiverId).orElseThrow();
        String content = null;

        if (notificationType == NotificationType.LIKE) {
            content = String.format("%s님이 %s님 영상에 좋아요를 눌렀습니다.", sender.getUsername(), receiver.getUsername());
        } else if (notificationType == NotificationType.FOLLOW) {
            content = String.format("%s님이 %s님을 팔로우하기 시작했습니다.", sender.getUsername(), receiver.getUsername());
        } else if (notificationType == NotificationType.COMMENT){
            content = String.format("%s님이 %s님의 영상에 댓글을 남겼습니다\n%s", sender.getUsername(), receiver.getUsername(), comment);
        }

        Notification notification = Notification.builder()
                .receiver(receiver)
                .sender(sender)
                .notificationType(notificationType)
                .content(content)
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        String memberId = String.valueOf(receiver.getId());
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(memberId);

        sseEmitters.forEach(
                (emitterId, emitter) -> {
                    sendToClient("sse", emitter, emitterId, NotificationResponseDTO.from(notification));
                }
        );
    }


    private void sendToClient(String type, SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name(type)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
        }
    }

    public NotificationCountResponseDTO getMyNotifications(Long memberId) {
        return notificationRepository.QcountUnreadNotifications(memberId);
    }

    public Page<NotificationContentResponseDTO> getMyNotificationContent(String type, Pageable pageable, Long memberId) {

        NotificationType notificationType = null; // 기본값은 null (전체 조회)
//        log.info("type={}", type);
        if (StringUtils.hasText(type)) {
            try {
                notificationType = NotificationType.valueOf(type.toUpperCase());
//                log.info("notificationType={}", notificationType);
            } catch (IllegalArgumentException e) {
                // 없는 타입을 호출할 경우
            }
        }


        return notificationRepository.QfindMyNotificationsContent(notificationType, pageable, memberId);
    }

    public Long countMyNotifications(Long memberId) {
        return notificationRepository.QcountAllUnreadNotifications(memberId);
    }

    @Transactional
    public void readNotifications(List<Long> notificationIds) {

        if (notificationIds == null || notificationIds.isEmpty()) {
            return;
        }

        notificationRepository.QmarkAsReadByIds(notificationIds);
    }
}
