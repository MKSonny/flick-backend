package pro.Flick.follow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.Flick.entity.Follower;
import pro.Flick.follow.dto.FollowRequestDTO;
import pro.Flick.notification.NotificationService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/followers")
@RequiredArgsConstructor
public class FollowControllerV3 {

    private final FollowService followService;
    private final NotificationService notificationService;


    @PostMapping
    public void addFollower(@RequestBody FollowRequestDTO requestDTO) {
        log.info("requestDTO={}", requestDTO);
        followService.memberAFollowsMemberBUsingRef(requestDTO.getUserId(), requestDTO.getFollower_user_id());
        notificationService.sendFollowNotification(requestDTO.getFollower_user_id(), requestDTO.getUserId());
    }


    @DeleteMapping("/{userId}/{followerUserId}")
    public void deleteFollowingV2(@PathVariable("userId") Long userId,
                                  @PathVariable("followerUserId") Long followerUserId) {
        followService.deleteFollower(userId, followerUserId);
    }
}
