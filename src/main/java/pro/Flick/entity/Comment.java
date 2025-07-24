package pro.Flick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
public class Comment {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 관계 설정 필요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video; // 관계 설정 필요

    private String text;

    @CreatedDate
    private LocalDateTime createdAt;

    public Comment(Member member, Video video, String text, LocalDateTime createdAt) {
        this.member = member;
        this.video = video;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Comment() {
    }
}
