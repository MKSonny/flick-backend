package pro.Flick.file;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.Flick.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {
}
