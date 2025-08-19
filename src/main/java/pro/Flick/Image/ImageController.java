package pro.Flick.Image;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pro.Flick.entity.UploadFile;
import pro.Flick.file.FileStore;
import pro.Flick.repsository.MemberJpaRepository;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final MemberJpaRepository memberJpaRepository;
    private final FileStore fileStore;

    @PostMapping("/file/image/upload")
    public String downloadProfileImage(@RequestParam("file") MultipartFile file,
                                       @RequestParam("userId") String userId) throws IOException {
        UploadFile storedFile = fileStore.storeFile(file); // 직접 구현한 저장 로직
        String fileStoreFullPath = fileStore.getFullPath(storedFile.getStoreFileName());
        memberJpaRepository.updateProfileImage(userId, fileStoreFullPath);
        return fileStoreFullPath;
    }
}
