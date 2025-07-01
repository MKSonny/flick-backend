package pro.Flick.controller.dto;

import lombok.Data;
import pro.Flick.entity.Member;

@Data
public class GetMemberByIdResponseDto {
    private Long id;
    private String username;
    private String email;

    public GetMemberByIdResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
    }
}
