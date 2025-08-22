package pro.Flick.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setter
@Builder
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username; // 유니크 제약 조건 추가 필요
    private String email; // 유니크 제약 조건 추가 필요
    private String password;

    @CreatedDate
    private LocalDateTime createTime;
    // == 주의 == 연관관계 매핑 시작

    /*
    "나는 Video 엔티티의 member 필드에 의해 매핑되었다.
     나는 외래 키 관리에 관여하지 않고 오직 조회만 하겠다" 라는 의미
     */
    @OneToMany(mappedBy = "member")
    private List<Video> videos = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Likes> likes = new ArrayList<>();

//    private String profileImageUri = "/Users/son/Desktop/Flick_Files/react-log.png";

    // 양방향 연관관계 비추
//    @OneToMany(mappedBy = "member")
//    private List<Follower> followers = new ArrayList<>();
//
//    @OneToMany(mappedBy = "follower")
//    private List<Follower> following = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Chat> chats = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "file_id")
    private File file;

    public String getProfileImageUri() {
        if (this.file != null) {
            return this.file.getStoredFileName();
        }
        // 프로필 이미지가 없는 경우 기본 이미지 경로를 반환
        return "/default_profile.png";
    }

    public Member(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
