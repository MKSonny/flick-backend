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
    @JoinColumn(name = "followed_id")
    private Member followed;

    // 팔로우 한 사람, 나?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private Member following;

    @CreatedDate
    private LocalDateTime createdAt;


    public Follower(Member memberA, Member memberB, LocalDateTime createdAt) {
        this.following = memberA;
        this.followed = memberB;
        this.createdAt = createdAt;
    }

    public Follower() {
    }
}
