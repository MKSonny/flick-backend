package pro.Flick.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.CreatedDate;
//import pro.Flick.file.UploadFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Video {

    @Id @GeneratedValue
    private Long id;

//    @Version
//    private Long version;

    private String title;
    private String uri;

    private String thumbnailStoreFileName; // 추가된 필드

    public void setThumbnailStoreFileName(String thumbnailStoreFileName) {
        this.thumbnailStoreFileName = thumbnailStoreFileName;
    }

//    private UploadFile attachFile;
//    private String uploadFileName; // 유저가 같은 파일 이름을 전송할 수 있으므로
//    private String storeFileName; // UUID로 생성

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 관계 설정 필요
//    private Member likedMembers; // 관계 설정 필요// on delete cascade 추가 필요
    @OneToMany(mappedBy = "video")
    private List<Likes> likes = new ArrayList<>();

    @Builder.Default
    private Long likesCount = 0L;

    public void incrementLikesCount() {
        likesCount += 1;
    }

    public void decrementLikesCount() {
        likesCount -= 1;
    }

    @CreatedDate
    private LocalDateTime createdAt;


    public void addLikes(Likes likes) {
        likes.setVideo(this);
        this.likes.add(likes);
    }
//    public Video(String title, String uri, Member member) {
//        this.title = title;
//        this.uri = uri;
//        this.member = member;
//    }
//
//    public Video() {
//    }
}
