package pro.Flick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Follower> followers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Chat> chats = new ArrayList<>();


    public Member(String email) {
        this.email = email;
    }

    public Member(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Member(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
