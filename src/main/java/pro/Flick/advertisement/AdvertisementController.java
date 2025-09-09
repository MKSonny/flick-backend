package pro.Flick.advertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.Flick.advertisement.dto.AdInfoResponseDTO;

@RequestMapping("/ad")
@RestController
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @GetMapping("/info/{videoId}")
    public AdInfoResponseDTO getAdInfo(@PathVariable Long videoId) {
        return advertisementService.getInfo(videoId);
    }
}
