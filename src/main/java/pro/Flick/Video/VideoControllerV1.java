//package pro.Flick.Video;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.web.bind.annotation.*;
//import pro.Flick.Video.dto.VideoWithMemberAndFollowerInfoDtoV3;
//import pro.Flick.Video.dto.VideoWithMemberDto;
//import pro.Flick.Video.dto.VideoWithMemberDtoV2;
//import pro.Flick.Video.service.VideoService;
//import pro.Flick.entity.Video;
//import pro.Flick.file.FileStore;
//
//import java.net.MalformedURLException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@RestController
//@RequestMapping("/videos-v1")
//@RequiredArgsConstructor
//public class VideoControllerV1 {
//
//    private final FileStore fileStore;
//    private final VideoRepository videoRepository;
//    private final VideoService videoService;
//
//
//    /**
//     * 저장된 비디오 파일을 클라이언트에게 스트리밍합니다.
//     * 파일 시스템에 저장된 파일명을 URL 경로로 받아 해당 파일을 UrlResource로 반환합니다.
//     *
//     * @param fileName 스토리지에 저장된 실제 파일명 (예: a4e2-f12b-3c4d.mov)
//     * @return 비디오 파일에 대한 Resource 객체
//     * @throws MalformedURLException 파일 경로가 유효하지 않은 URL 형식일 경우 발생
//     */
//    @ResponseBody
//    @GetMapping("/{fileName}")
//    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
//        return new UrlResource("file:" + fileStore.getFullPath(fileName));
//    }
//
//    /**
//     * userId를 받으면 해당 유저가 올린 영상들의 목록을 반환합니다.
//     * 프로필 화면에 유저가 올린 영상들의 목록을 보여주기 위한 함수입니다.
//     *
//     * @param userId
//     * @return 특정 유저가 올린 영상들의 목록
//     */
//    @GetMapping("/{userId}") // PathVariable로 userId를 넘기는 것이 안전한가?
//    public List<VideoWithMemberDto> getVideosByUserIdV3(@PathVariable String userId) {
//        List<Video> videos = videoRepository.findVideosByMemberIdWithMember(userId);
//        List<VideoWithMemberDto> dtos = new ArrayList<>();
//
//        for (Video video : videos) {
//            dtos.add(VideoWithMemberDto.fromVideoAndMember(video, video.getMember()));
//        }
//        return dtos;
//    }
//
//    /**
//     * 홈 화면의 비디오 목록들을 나열해서 보여줍니다.
//     * 페이징과 정렬 기능 추가가 필요합니다.
//     *
//     * @return 영상 파일 목록과 해당 영상을 올린 유저의 정보
//     */
//    @GetMapping("/get-all-videos")
//    public Page<VideoWithMemberDto> getAllVideosV3(@PageableDefault(size = 5) Pageable pageable) {
//        log.info("getAllVideosV3 start");
//        Page<Video> videos = videoRepository.findAllVideos(pageable);
//        return videos.map(video -> VideoWithMemberDto.fromVideoAndMember(video, video.getMember()));
//    }
//
//    @GetMapping("/v2/get-all-videos")
//    public Page<VideoWithMemberDtoV2> getAllVideosV4(@PageableDefault(size = 5) Pageable pageable) {
//        log.info("start getAllVideosV4()");
//        return videoService.getVideoInfo(pageable);
//    }
//
//    @GetMapping("/v3/get-all-videos")
//    public Page<VideoWithMemberAndFollowerInfoDtoV3> getAllVideosV5(@PageableDefault(size = 5) Pageable pageable, @RequestParam("userId") Long userId) {
//        return videoService.getVideoInfoV2(pageable, userId);
//    }
//}
