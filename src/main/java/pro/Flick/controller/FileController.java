package pro.Flick.controller;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.TE;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.file.FileStore;
import pro.Flick.repsository.FileRepository;
import pro.Flick.repsository.MemberRepository;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileStore fileStore;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        Member member = memberRepository.findMember("email", "123");
        fileRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
    }

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
            videos.add(new Temp(video.getId(), video.getTitle(), video.getUri()));
        }
        return videos;
    }


    @Data
    static class Temp {
        private Long id;
        private String title;
        private String uri;

        public Temp(Long id, String title, String uri) {
            this.id = id;
            this.title = title;
            this.uri = uri;
        }
    }

}
