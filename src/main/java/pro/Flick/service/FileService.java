package pro.Flick.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.Flick.Video.VideoJpaRepository;

@Service
@RequiredArgsConstructor
public class FileService {
    private final VideoJpaRepository fileRepository;

//    public void saveVideo(String videoTitle, String uuidTitle, Member member) {
//        "http://127.0.0.1:8080/video/" +  videoUri;
//        fileRepository.saveVideo(videoTitle, uuidTitle, member);
//    }
    //       fileRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
}
