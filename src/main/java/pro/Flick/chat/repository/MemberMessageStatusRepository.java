package pro.Flick.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.Flick.entity.MemberMessageStatus;

public interface MemberMessageStatusRepository extends JpaRepository<MemberMessageStatus, Long>, MemberMessageStatusRepositoryCustom {

}
