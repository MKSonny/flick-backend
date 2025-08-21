package pro.Flick.follow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FollowControllerV2 {

    private final FollowService followService;

    @PostMapping("/followers")
    public void addFollower(@RequestBody FollowRequestDTO requestDTO) {
        followService.memberAFollowsMemberBUsingRef(requestDTO.getUserId(), requestDTO.getFollower_user_id());
    }

    @DeleteMapping("/followers")
    public void deleteFollowing(@RequestParam Long userId, @RequestParam Long follower_user_id) {
        followService.deleteFollower(userId, follower_user_id);
    }
}
