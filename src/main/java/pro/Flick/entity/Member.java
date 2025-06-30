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

    private String username;
    private String email;
    private LocalDateTime createTime;
}
