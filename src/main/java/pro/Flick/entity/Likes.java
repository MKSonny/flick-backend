package pro.Flick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Likes {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 관계 설정 필요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video; // 관계 설정 필요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @CreatedDate
    private LocalDateTime createdAt;

    public Likes(Member member, Comment comment, LocalDateTime createdAt) {
        this.member = member;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Likes(Member member, Video video, LocalDateTime createdAt) {
        this.member = member;
        this.video = video;
        this.createdAt = createdAt;
    }


    public Likes() {
    }
}
