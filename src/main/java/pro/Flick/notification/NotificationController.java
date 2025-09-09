package pro.Flick.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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


    @GetMapping("/my_notification_content/{id}/{type}")
    public Page<NotificationContentResponseDTO> myNotificationsContent(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                       @PathVariable Long id, @PathVariable(required = false) String type) {
        return notificationService.getMyNotificationContent(type, pageable, id);
    }



    @PostMapping("/read")
    public void readNotifications(@RequestBody List<Long> notificationIds) {
        log.info("notificationIds={}", notificationIds);
        notificationService.readNotifications(notificationIds);
    }
}
