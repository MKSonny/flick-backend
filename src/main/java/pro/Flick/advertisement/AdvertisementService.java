package pro.Flick.advertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.Flick.advertisement.dto.AdInfoResponseDTO;
import pro.Flick.advertisement.repository.AdvertisementRepository;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    public AdInfoResponseDTO getInfo(Long videoId) {
        return advertisementRepository.QfindAdInfo(videoId);
    }
}