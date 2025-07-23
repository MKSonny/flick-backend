package pro.Flick.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import pro.Flick.entity.Member;
import pro.Flick.file.FileStore;
import pro.Flick.repsository.FileRepository;
import pro.Flick.repsository.MemberRepository;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileStore fileStore;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        Member member = memberRepository.findMember("email", "123");
        fileRepository.saveVideo("test", fileStore.getFullPath("test"), member);
    }

    // 이미지 조회를 위한 이미지 다운로드
    @ResponseBody
    @GetMapping("/files/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(fileName));
    }
}
