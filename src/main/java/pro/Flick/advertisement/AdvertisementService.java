package pro.Flick.advertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.Flick.advertisement.dto.AdInfoResponseDTO;
import pro.Flick.advertisement.repository.AdvertisementRepository;
import pro.Flick.api_response.CustomException;
import pro.Flick.api_response.ErrorCode;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    public AdInfoResponseDTO getInfo(Long videoId) {
        AdInfoResponseDTO adInfo = advertisementRepository.QfindAdInfo(videoId);
        if (adInfo == null) {
            throw new CustomException(ErrorCode.AD_INFO_NOT_FOUND);
        }
        return adInfo;
    }
}