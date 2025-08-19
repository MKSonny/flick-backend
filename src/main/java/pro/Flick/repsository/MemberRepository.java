package pro.Flick.repsository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.Flick.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
