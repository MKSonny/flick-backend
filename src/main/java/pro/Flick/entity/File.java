package pro.Flick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import pro.Flick.member.entity.Member;

@Entity
@Getter
public class File {

    @Id
    @GeneratedValue
    private Long id;

    private String storedFileName;

    @OneToOne(mappedBy = "file", fetch = FetchType.LAZY)
    private Member member;

    public File() {
    }

    public File(String storedFileName, Member member) {
        this.storedFileName = storedFileName;
        this.member = member;
    }
}
