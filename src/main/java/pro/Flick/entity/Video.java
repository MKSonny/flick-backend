package pro.Flick.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
//import pro.Flick.file.UploadFile;

import java.time.LocalDateTime;

@Entity
public class Video {

    @Id @GeneratedValue
    private Long id;
    private String title;
    private String uri;

//    private UploadFile attachFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 관계 설정 필요
//    private Member likedMembers; // 관계 설정 필요// on delete cascade 추가 필요


    @CreatedDate
    private LocalDateTime createdTime;

}
