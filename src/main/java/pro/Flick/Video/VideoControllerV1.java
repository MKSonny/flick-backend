package pro.Flick.Video;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import pro.Flick.entity.Video;
import pro.Flick.file.FileStore;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VideoControllerV1 {

    private final FileStore fileStore;
    private final VideoJpaRepository fileRepository;
    private final VideoRepository videoRepository;


    /**
     * 저장된 비디오 파일을 클라이언트에게 스트리밍합니다.
     * 파일 시스템에 저장된 파일명을 URL 경로로 받아 해당 파일을 UrlResource로 반환합니다.
     *
     * @param fileName 스토리지에 저장된 실제 파일명 (예: a4e2-f12b-3c4d.mov)
     * @return 비디오 파일에 대한 Resource 객체
     * @throws MalformedURLException 파일 경로가 유효하지 않은 URL 형식일 경우 발생
     */
    @ResponseBody
    @GetMapping("/video/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(fileName));
    }

    /**
     * 홈 화면의 비디오 목록들을 나열해서 보여줍니다.
     * 페이징과 정렬 기능 추가가 필요합니다.
     *
     * @return 영상 파일 목록과 해당 영상을 올린 유저의 정보
     */
    @GetMapping("/videos")
    public List<VideoController.VideoWithMemberDto> getAllVideosV2() {
        List<Video> videos = videoRepository.findVideosWithMember();

        List<VideoController.VideoWithMemberDto> dtos = new ArrayList<>();
        for (Video video : videos) {
            dtos.add(VideoController.VideoWithMemberDto.fromVideoAndMember(video, video.getMember()));
        }
        return dtos;
    }


    /**
     * userId를 받으면 해당 유저가 올린 영상들의 목록을 반환합니다.
     * 프로필 화면에 유저가 올린 영상들의 목록을 보여주기 위한 함수입니다.
     *
     * @param userId
     * @return 특정 유저가 올린 영상들의 목록
     */
    @GetMapping("/videos/{userId}") // PathVariable로 userId를 넘기는 것이 안전한가?
    public List<VideoController.VideoWithMemberDto> getVideosByUserIdV3(@PathVariable String userId) {
        List<Video> videos = videoRepository.findVideosByMemberIdWithMember(userId);
        List<VideoController.VideoWithMemberDto> dtos = new ArrayList<>();

        for (Video video : videos) {
            dtos.add(VideoController.VideoWithMemberDto.fromVideoAndMember(video, video.getMember()));
        }
        return dtos;
    }
}
