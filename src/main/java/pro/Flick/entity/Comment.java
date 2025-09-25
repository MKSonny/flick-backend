package pro.Flick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import pro.Flick.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Comment {
    @Id @GeneratedValue
    private Long id;

//    @Version
//    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 관계 설정 필요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video; // 관계 설정 필요

    // 왜 이걸 Member 객체로 해야 하는가?
//    private String video_member_id;

    private String text;

    private Long likesCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

//    public void setParent(Comment parent) {
//        this.parent = parent;
//        parent.getChildren().add(this);
//    }

    public void incrementCommentLikesCount() {
        likesCount += 1;
    }

    public void decrementLikesCount() {
        likesCount -= 1;
    }

    @CreatedDate
    private LocalDateTime createdAt;

    public Comment(Member member, Video video, String text, LocalDateTime createdAt) {
        this.member = member;
        this.video = video;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Comment(Member member, Video video, String text, Comment parent, LocalDateTime createdAt) {
        this.member = member;
        this.video = video;
        this.text = text;
        this.parent = parent;
        this.createdAt = createdAt;
    }

    //    public Comment(Member member, Video video, String text, LocalDateTime createdAt) {
//        this.member = member;
//        this.video = video;
//        this.text = text;
//        this.createdAt = createdAt;
//    }

    public Comment() {
    }
}
