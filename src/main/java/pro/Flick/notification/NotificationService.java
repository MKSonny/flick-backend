package pro.Flick.notification;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pro.Flick.entity.Member;
import pro.Flick.entity.Notification;
import pro.Flick.entity.NotificationType;
import pro.Flick.member.repository.MemberRepository;

import java.io.IOException;
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
}
