package pro.Flick.Video;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pro.Flick.Video.service.VideoService;

@RestController
@RequiredArgsConstructor
public class VideoControllerJMeter {

    private final VideoService videoService;

    @PostMapping("/jmeter/video-likes")
    public void addLikesTest(@RequestBody AddLikesTestRequestDTO requestDTO) {
        videoService.addLikesV2(requestDTO.getUserId(), requestDTO.getVideoId());
    }

    @Data
    static class AddLikesTestRequestDTO {
        private String userId;
        private String videoId;

        public AddLikesTestRequestDTO(String userId, String videoId) {
            this.userId = userId;
            this.videoId = videoId;
        }
    }
}
