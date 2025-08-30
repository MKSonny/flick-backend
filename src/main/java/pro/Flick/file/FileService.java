package pro.Flick.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.Flick.entity.File;
import pro.Flick.entity.Member;
import pro.Flick.entity.UploadFile;
import pro.Flick.member.repository.MemberRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStore fileStore;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;

    public String storeImage(MultipartFile file, Long memberId) throws IOException {

        UploadFile storedFile = fileStore.storeFile(file);
        String storeFileName = storedFile.getStoreFileName();
        String fileStoreFullPath = fileStore.getFullPath(storeFileName);

        Member member = memberRepository.findById(memberId).orElseThrow();
        File imageFile = new File(storeFileName, member);

        member.setFile(imageFile);

        fileRepository.save(imageFile);

        return fileStoreFullPath;
    }
}
