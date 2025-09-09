package pro.Flick.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.Flick.entity.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, AdvertisementRepositoryCustom {
}
