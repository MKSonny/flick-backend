package pro.Flick.controller.dto;

import lombok.Data;
import pro.Flick.entity.Member;

@Data
public class GetMemberResponseDto {
    private Long id;
    private String email;

    public GetMemberResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
    }
}
