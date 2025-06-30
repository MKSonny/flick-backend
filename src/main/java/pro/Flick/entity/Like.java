package pro.Flick.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class Like {

    @Id @GeneratedValue
    private Long id;

    private Member member; // 관계 설정 필요
    private Video video; // 관계 설정 필요

    @CreatedDate
    private LocalDateTime createdAt;
}
