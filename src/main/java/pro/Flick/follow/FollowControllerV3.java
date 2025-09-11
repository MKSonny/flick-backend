package pro.Flick.follow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.Flick.api_response.ApiResponse;
import pro.Flick.entity.Follower;
import pro.Flick.entity.NotificationType;
import pro.Flick.follow.dto.FollowRequestDTO;
import pro.Flick.follow.dto.FollowResponseDto;
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
    public ApiResponse<FollowResponseDto> addFollower(@RequestBody FollowRequestDTO requestDTO) {
        log.info("requestDTO={}", requestDTO);
        FollowResponseDto dto = followService.memberAFollowsMemberBUsingRef(requestDTO.getUserId(), requestDTO.getFollower_user_id());
        notificationService.send(requestDTO.getFollower_user_id(), requestDTO.getUserId(), NotificationType.FOLLOW, null);
        return ApiResponse.ok(dto);
    }


    @DeleteMapping("/{userId}/{followerUserId}")
    public ApiResponse<Void> deleteFollowingV2(@PathVariable("userId") Long userId,
                                  @PathVariable("followerUserId") Long followerUserId) {
        followService.deleteFollower(userId, followerUserId);
        return ApiResponse.ok(null);
    }
}
