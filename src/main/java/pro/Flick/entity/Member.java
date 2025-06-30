package pro.Flick.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username; // 유니크 제약 조건 추가 필요
    private String email; // 유니크 제약 조건 추가 필요
    private LocalDateTime createTime;
}
