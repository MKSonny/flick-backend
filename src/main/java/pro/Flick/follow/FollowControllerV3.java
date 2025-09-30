package pro.Flick.follow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pro.Flick.api_response.ApiResponse;
import pro.Flick.entity.Follower;
import pro.Flick.entity.NotificationType;
import pro.Flick.follow.dto.FollowRequestDTO;
import pro.Flick.follow.dto.FollowResponseDto;
import pro.Flick.member.CustomUserDetails;
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
    public ApiResponse<FollowResponseDto> addFollower(@AuthenticationPrincipal CustomUserDetails user, @RequestBody FollowRequestDTO requestDTO) {
        log.info("requestDTO={}", requestDTO);
        FollowResponseDto dto = followService.memberAFollowsMemberBUsingRef(user.getId(), requestDTO.getFollower_user_id());
        notificationService.send(requestDTO.getFollower_user_id(), user.getId(), NotificationType.FOLLOW, null);
        return ApiResponse.ok(dto);
    }


    @DeleteMapping("/{followerUserId}")
    public ApiResponse<Void> deleteFollowingV2(
                                @AuthenticationPrincipal CustomUserDetails user,
                                  @PathVariable("followerUserId") Long followerUserId) {
        followService.deleteFollower(user.getId(), followerUserId);
        return ApiResponse.ok(null);
    }
}
