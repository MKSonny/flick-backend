package pro.Flick.advertisement.repository;

import pro.Flick.advertisement.dto.AdInfoResponseDTO;

public interface AdvertisementRepositoryCustom {
    AdInfoResponseDTO QfindAdInfo(Long videoId);
}
