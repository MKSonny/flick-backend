package pro.Flick.follow.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FollowResponseDto(
        Long id,
        Long followerId,
        Long followingId,
        LocalDateTime createdAt
) {

}
