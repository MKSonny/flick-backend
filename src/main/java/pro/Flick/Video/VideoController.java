package pro.Flick.Video;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Member;
import pro.Flick.entity.UploadFile;
import pro.Flick.entity.Video;
import pro.Flick.file.FileStore;
import pro.Flick.repsository.MemberRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VideoController {

    private final FileStore fileStore;
    private final VideoJpaRepository fileRepository;
    private final VideoRepository videoRepository; // spring data jpa 사용
    private final MemberRepository memberRepository;



    // 이미지 조회를 위한 이미지 다운로드
    @ResponseBody
    @GetMapping("/video/{fileName}") // 파일 이름만 넘겨주면 내 서버에서 영상을 찾아서 넘겨줌
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(fileName));
    }

    //    @GetMapping("/videos")
    public List<Temp> getAllVideos() {
//        List<Video> allVideos = fileRepository.getAllVideos();
        List<Video> allVideos = videoRepository.findAll();

        List<Temp> videos = new ArrayList<>();
        for (Video video : allVideos) {
            log.info("video={}", video.getUri());
            /**
             * 주의!
             * 이후 video.getMember()를 join하는 건지 어떻게 하는 건지 확인 반드시 필요
             */
            videos.add(new Temp(video.getId(), video.getTitle(), video.getUri(), new GetMemberByIdResponseDto(video.getMember())));
        }
        return videos;
    }

    // join fetch를 사용한 모든 영상을 가져오는 코드
    @GetMapping("/videos")
    public List<VideoWithMemberDto> getAllVideosV2() {
//        List<Video> videos = fileRepository.findVideosWithMember();
        List<Video> videos = videoRepository.findVideosWithMember();

        List<VideoWithMemberDto> dtos = new ArrayList<>();
        for (Video video : videos) {
            log.info("video={}", video.getUri());
            /**
             * 🚨 경고: N+1 문제 발생 지점 🚨
             * * 위에서 가져온 videos 리스트를 순회하며 `video.getMember()`를 호출할 때마다,
             * 지연 로딩(Lazy Loading)으로 인해 매번 새로운 `SELECT` 쿼리가 데이터베이스로 전송됩니다.
             * * - '1'번 쿼리: fileRepository.getAllVideos() (모든 Video 조회)
             * - '+N'번 쿼리: for 루프 안에서 video.getMember() 호출 시 (N개의 Member 조회)
             * * 하이버네이트의 1차 캐시 때문에 쿼리가 보이지 않을 수도 있지만, 근본적인 성능 문제는 해결되지 않습니다.
             * * ✅ 해결 방법:
             * fileRepository에서 `getAllVideos()` 대신 '페치 조인(Fetch Join)'을 사용해
             * Video와 Member를 단 한 번의 쿼리로 함께 조회해야 합니다.
             */
            dtos.add(VideoWithMemberDto.fromVideoAndMember(video, video.getMember()));
        }
        return dtos;
    }

    // 페이징 기능 추가 필요
//    @GetMapping("/videos/{userId}")
    public List<Temp> getVideosByUserId(@PathVariable String userId) {
        List<Video> videos = fileRepository.findVideoByMemberId(userId);


        List<Temp> dtoList = new ArrayList<>();

        for (Video video : videos) {
            dtoList.add(new Temp(video.getId(), video.getTitle(), video.getUri(), new GetMemberByIdResponseDto(video.getMember())));
        }

        return dtoList;
    }

    // member에 있는 videos를 직접 꺼낼수는 없을까
    // 프론트 엔드의 어느 tsx에서 호출되는지 알 수 없나
//    @GetMapping("/videos/{userId}")
    public List<Temp> getVideosByUserIdV2(@PathVariable String userId) {
        log.info("getVideosByUserIdV2 start");
        Member member = memberRepository.findMemberById(userId);
        List<Video> videos = member.getVideos();

        log.info("hello world={}", videos); // videos가 들어가 있다 언제 add 되었는지

        List<Temp> dtoList = new ArrayList<>();

        for (Video video : videos) {
            dtoList.add(new Temp(video.getId(), video.getTitle(), video.getUri(), new GetMemberByIdResponseDto(video.getMember())));
        }
        log.info("getVideosByUserIdV2 end");

        return dtoList;
    }
    /*
    <type>(<scope>): <subject>

    <blank line>

    <body: problem>

    <body: solution>

    * **변경 내용**: <detailed-change>
    * **개선 효과**: <benefit>
     */

    @GetMapping("/videos/{userId}") // PathVariable로 userId를 넘기는 것이 안전한가?
    public List<VideoWithMemberDto> getVideosByUserIdV3(@PathVariable String userId) {
//        List<Video> videos = member.getVideos(); 이런식으로 member의 영상들을 가져오는 것은 비추, 지연로딩이므로 N+1 문제 발생 가능
        log.info("API 호출: getVideosByUserIdV3, userId={}", userId);
//        List<Video> videos = fileRepository.findVideosByMemberIdWithMember(userId);
        List<Video> videos = videoRepository.findVideosByMemberIdWithMember(userId);
        List<VideoWithMemberDto> dtos = new ArrayList<>();

        for (Video video : videos) {
            dtos.add(VideoWithMemberDto.fromVideoAndMember(video, video.getMember()));
        }
        log.info("API 종료: getVideosByUserIdV3");
        return dtos;
    }

    @PostMapping("/videos")
    public List<Temp> getAllVideos(@RequestBody List<Long> memberIds) {
//        List<Video> videos = fileRepository.findVideoByMemberId(memberIds);
        List<Video> videos = videoRepository.findVideoByMemberIds(memberIds);

        List<Temp> dtoList = new ArrayList<>();

        for (Video video : videos) {
            dtoList.add(new Temp(video.getId(), video.getTitle(), video.getUri(), new GetMemberByIdResponseDto(video.getMember())));
        }

        return dtoList;
    }

    @Data
    public class UploadFileResponse {
        private String fileName;
        private String storePath;

        public UploadFileResponse(String fileName, String storePath) {
            this.fileName = fileName;
            this.storePath = storePath;
        }

        // getters 생략
    }

    @Data
    static class VideoWithMemberDto {
        private Long id;
        private String title;
        private String uri;
        private GetMemberByIdResponseDto member;

        public static VideoWithMemberDto fromVideoAndMember(Video video, Member member) {
            VideoWithMemberDto dto = new VideoWithMemberDto();
            dto.id = video.getId();
            dto.title = video.getTitle();
            dto.uri = video.getUri();
            dto.member = new GetMemberByIdResponseDto(member);

            return dto;
        }
    }

    @Data
    static class Temp {
        private Long id;
        private String title;
        private String uri;
        private GetMemberByIdResponseDto member;

        public Temp(Long id, String title, String uri, GetMemberByIdResponseDto member) {
            this.id = id;
            this.title = title;
            this.uri = uri;
            this.member = member;
        }
    }

}
