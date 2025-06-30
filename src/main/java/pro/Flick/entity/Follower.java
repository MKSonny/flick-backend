package pro.Flick.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class Follower {

    @Id @GeneratedValue
    private Long id;
    private Member member;

    @CreatedDate
    private LocalDateTime createdAt;
}
