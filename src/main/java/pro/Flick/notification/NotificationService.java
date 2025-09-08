package pro.Flick.notification;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pro.Flick.entity.Member;
import pro.Flick.entity.Notification;
import pro.Flick.entity.NotificationType;
import pro.Flick.member.repository.MemberRepository;
import pro.Flick.notification.repository.EmitterRepository;
import pro.Flick.notification.repository.NotificationRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;

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

    public void sendVideoLikeNotification(Long receiverId, Long senderId) {
        Member sender = memberRepository.findById(senderId).orElseThrow();
        Member receiver = memberRepository.findById(receiverId).orElseThrow();

        String content = String.format("%s님이 %s님 영상에 좋아요를 눌렀습니다.", sender.getUsername(), receiver.getUsername());

        send(receiverId, NotificationType.LIKE, content);
    }


    @Transactional
    private void send(Long receiverId, NotificationType notificationType, String content) {
        Member receiver = memberRepository.findById(receiverId).orElseThrow();

        Notification notification = Notification.builder()
                .receiver(receiver)
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

    public Long getMyNotifications(Long memberId) {
        return notificationRepository.QcountUnreadNotifications(memberId);
    }

    public Page<NotificationContentResponseDTO> getMyNotificationContent(Pageable pageable, Long memberId) {
        return notificationRepository.QfindMyNotificationsContent(pageable, memberId);
    }

    @Transactional
    public void readNotifications(List<Long> notificationIds) {
        notificationRepository.QmarkAsReadByIds(notificationIds);
    }
}
