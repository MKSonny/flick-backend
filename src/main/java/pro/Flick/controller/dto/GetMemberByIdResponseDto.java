package pro.Flick.controller.dto;

import lombok.Data;
import pro.Flick.entity.Member;

import java.time.LocalDateTime;

@Data
public class GetMemberByIdResponseDto {
    private Long id;
    private String username;
    private String email;
    private String profile_image_uri;
    private LocalDateTime created_at;

    public GetMemberByIdResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.profile_image_uri = member.getProfileImageUri();
        this.created_at = member.getCreateTime();
    }
}
