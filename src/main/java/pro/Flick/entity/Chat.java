package pro.Flick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
public class Chat {

    @Id @GeneratedValue
    private Long id;

    private String usersKey; // 방번호?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 관계 설정 필요

    private String text;

    @CreatedDate
    private LocalDateTime localDateTime;

    public Chat(String usersKey, Member member, String text, LocalDateTime localDateTime) {
        this.usersKey = usersKey;
        this.member = member;
        this.text = text;
        this.localDateTime = localDateTime;
    }

    public Chat() {
    }
}
