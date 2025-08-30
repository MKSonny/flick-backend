package pro.Flick.likes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Member;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LikeOnMyVideoResponseDTO {
    private GetMemberByIdResponseDto user;
    private LocalDateTime createdAt;

    public LikeOnMyVideoResponseDTO(LocalDateTime createdAt, Member member) {
        this.createdAt = createdAt;
        this.user = new GetMemberByIdResponseDto(member);
    }
}
