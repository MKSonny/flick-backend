package pro.Flick.member;

import lombok.Data;
import pro.Flick.entity.Member;

import java.time.LocalDateTime;

@Data
public class FindMembersByUsernameResponseDto {
    private LocalDateTime created_at;
    private String email;
    private Long id;
    private String username;

    public FindMembersByUsernameResponseDto(Member member) {
        this.created_at = member.getCreateTime();
        this.email = member.getEmail();
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
