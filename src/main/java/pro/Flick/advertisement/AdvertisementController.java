package pro.Flick.advertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.Flick.advertisement.dto.AdInfoResponseDTO;
import pro.Flick.api_response.ApiResponse;

@RequestMapping("/ad")
@RestController
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @GetMapping("/info/{videoId}")
    public ApiResponse<AdInfoResponseDTO> getAdInfo(@PathVariable Long videoId) {
        return ApiResponse.ok(advertisementService.getInfo(videoId));
    }
}
