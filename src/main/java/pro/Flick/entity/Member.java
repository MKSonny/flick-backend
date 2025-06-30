package pro.Flick.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username; // 유니크 제약 조건 추가 필요
    private String email; // 유니크 제약 조건 추가 필요

    @CreatedDate
    private LocalDateTime createTime;




    public Member(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
