package pro.Flick.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Item;
import pro.Flick.entity.UploadFile;
import pro.Flick.entity.Video;
import pro.Flick.file.FileStore;
import pro.Flick.repsository.FileRepository;
import pro.Flick.repsository.MemberRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileStore fileStore;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;



    // 이미지 조회를 위한 이미지 다운로드
    @ResponseBody
    @GetMapping("/video/{fileName}") // 파일 이름만 넘겨주면 내 서버에서 영상을 찾아서 넘겨줌
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(fileName));
    }

    @GetMapping("/videos")
    public List<Temp> getAllVideos() {
        List<Video> allVideos = fileRepository.getAllVideos();

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

    @PostMapping("/videos")
    public List<Temp> getAllVideos(@RequestBody List<Long> memberIds) {
        List<Video> videos = fileRepository.findVideoByMemberId(memberIds);
        List<Temp> dtoList = new ArrayList<>();

        for (Video video : videos) {
            dtoList.add(new Temp(video.getId(), video.getTitle(), video.getUri(), new GetMemberByIdResponseDto(video.getMember())));
        }

        return dtoList;
    }

    @PostMapping("/file/image/upload")
    public String downloadProfileImage(@RequestParam("file") MultipartFile file,
                                       @RequestParam("userId") String userId) throws IOException {
        UploadFile storedFile = fileStore.storeFile(file); // 직접 구현한 저장 로직
        log.info("fileStore={}", fileStore.getFullPath(storedFile.getStoreFileName()));
        String fileStoreFullPath = fileStore.getFullPath(storedFile.getStoreFileName());
        memberRepository.updateProfileImage(userId, fileStoreFullPath);
        return fileStoreFullPath;
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
