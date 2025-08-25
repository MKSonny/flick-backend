package pro.Flick.Image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.Flick.entity.UploadFile;
import pro.Flick.file.FileService;
import pro.Flick.file.FileStore;
import pro.Flick.member.MemberRepository;

import java.io.IOException;
import java.net.MalformedURLException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController {
    private final MemberRepository memberRepository;
    private final FileStore fileStore;
    private final FileService fileService;


    @PostMapping("/file/image/upload")
    public String downloadProfileImage(@RequestParam("file") MultipartFile file,
                                       @RequestParam("userId") Long userId) throws IOException {
//        UploadFile storedFile = fileStore.storeFile(file); // 직접 구현한 저장 로직
//        String storeFileName = storedFile.getStoreFileName();
//        String fileStoreFullPath = fileStore.getFullPath(storeFileName);




//        memberJpaRepository.updateProfileImage(userId, storeFileName);
//        return fileStoreFullPath;
        return fileService.storeImage(file, userId);
    }

    // 이미지 조회를 위한 이미지 다운로드
    @ResponseBody
    @GetMapping("/image/{fileName}") // 파일 이름만 넘겨주면 내 서버에서 영상을 찾아서 넘겨줌
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(fileName));
    }
}
