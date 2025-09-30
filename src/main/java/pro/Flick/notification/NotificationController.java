package pro.Flick.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pro.Flick.member.CustomUserDetails;

import java.util.List;

@RequestMapping("/notification")
@RestController
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // id는 멤버 id
    @GetMapping(value = "/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long id) {

        log.info("asdfasfsdf={}", id);

        return notificationService.subscribe(id);
    }

    @GetMapping("/my_notification/{id}")
    public NotificationCountResponseDTO myNotifications(@PathVariable Long id) {
        return notificationService.getMyNotifications(id);
    }

    @GetMapping("/count_my_notification/{id}")
    public Long myNotificationsAll(@PathVariable Long id) {
        return notificationService.countMyNotifications(id);
    }

    @GetMapping("/count_my_notification-v2")
    public Long myNotificationsAllV2(@AuthenticationPrincipal CustomUserDetails user) {
        log.info("adfasfadfasdfsdf={}", user.getId());
        return notificationService.countMyNotifications(user.getId());
    }


    @GetMapping("/my_notification_content/{type}")
    public Page<NotificationContentResponseDTO> myNotificationsContent(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                       @AuthenticationPrincipal CustomUserDetails user, @PathVariable(required = false) String type) {
        return notificationService.getMyNotificationContent(type, pageable, user.getId());
    }



    @PostMapping("/read")
    public void readNotifications(@RequestBody List<Long> notificationIds) {
        log.info("notificationIds={}", notificationIds);
        notificationService.readNotifications(notificationIds);
    }
}
