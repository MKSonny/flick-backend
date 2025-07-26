package pro.Flick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
public class Follower {

    @Id @GeneratedValue
    private Long id;

    // 팔로우 당한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 팔로우 한 사람, 나?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private Member follower;

    @CreatedDate
    private LocalDateTime createdAt;

    public Follower(Member member, Member follower, LocalDateTime createdAt) {
        this.member = member;
        this.follower = follower;
        this.createdAt = createdAt;
    }

    public Follower() {
    }
}
