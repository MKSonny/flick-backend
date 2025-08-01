package pro.Flick.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
//import pro.Flick.file.UploadFile;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Video {

    @Id @GeneratedValue
    private Long id;
    private String title;
    private String uri;

//    private UploadFile attachFile;
//    private String uploadFileName; // 유저가 같은 파일 이름을 전송할 수 있으므로
//    private String storeFileName; // UUID로 생성

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 관계 설정 필요
//    private Member likedMembers; // 관계 설정 필요// on delete cascade 추가 필요


    @CreatedDate
    private LocalDateTime createdTime;

//    public Video(String title, String uri, Member member) {
//        this.title = title;
//        this.uri = uri;
//        this.member = member;
//    }
//
//    public Video() {
//    }
}
