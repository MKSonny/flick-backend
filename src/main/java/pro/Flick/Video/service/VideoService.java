package pro.Flick.Video.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pro.Flick.Video.VideoJpaRepository;
import pro.Flick.Video.VideoRepository;
import pro.Flick.Video.dto.VideoWithMemberDtoV2;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.file.FileStore;
import pro.Flick.repsository.LikesJpaRepository;
import pro.Flick.repsository.LikesRepository;
import pro.Flick.repsository.MemberRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoService {
    private final FileStore fileStore;
    private final VideoRepository videoRepository;
    private final LikesRepository likesRepository;
    private final LikesJpaRepository likesJpaRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void addLikes(String userId, String videoId) {
        Member member = memberRepository.findMemberById(userId);
        Video video = videoRepository.findById(Long.valueOf(videoId)).get();
        likesJpaRepository.addLike(member, video);
        videoRepository.incrementLikesCount(Long.valueOf(videoId));
    }

    public Page<VideoWithMemberDtoV2> getVideoInfo(Pageable pageable) {
        /*
            이렇게 작성하면 video의 수만큼 like엔티티에 쿼리가 들어간다 매우 비효율적
            처음부터 likesCount는 안되나
         */

        Page<Video> videos = videoRepository.findAllVideos(pageable);

        log.info("videoRepository.findAllVideos(pageable) start");
        //            Long likesCount = likesRepository.findLikesByVideoId(v.getId());
        return videos.map(VideoWithMemberDtoV2::new);

//        return videos.map(v -> {
//            Long likesCount = likesRepository.findLikesByVideoId(v.getId());
//            return new VideoWithMemberDtoV2(v, likesCount, v.getMember());
//        });
    }

    @Async
    public void createVideo(String fileName, Member member) {
        Video video = Video.builder()
                .title(fileName)
                .uri("http://127.0.0.1:8080/video/test.mov")
                .member(member)
                .build();

        // 입력 비디오 파일 경로
        // FileStore를 통해 실제 파일 시스템 경로를 가져와야 합니다.
        String inputPath = fileStore.getFullPath(fileName);

        // 생성될 썸네일 파일 이름과 전체 경로
        String thumbnailStoreFileName = createThumbnailStoreFileName(fileName);
        String outputPath = fileStore.getFullPath(thumbnailStoreFileName);

        try {
            // jcodec을 사용하여 비디오 파일에서 특정 프레임을 가져옵니다.
            // 150번째 프레임 (약 5초 지점, 30fps 기준)을 가져오도록 설정했습니다.
            Picture picture = FrameGrab.getFrameFromFile(new File(inputPath), 150);

            // Picture 객체를 BufferedImage로 변환합니다.
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);

            // 변환된 이미지를 JPG 파일로 저장합니다.
            ImageIO.write(bufferedImage, "jpg", new File(outputPath));

            log.info("jcodec을 이용한 썸네일 생성 완료: {}", outputPath);

            // DB에 썸네일 파일명 업데이트
            video.setThumbnailStoreFileName(thumbnailStoreFileName);
            videoRepository.save(video);

        } catch (Exception e) {
            log.error("jcodec 썸네일 생성 실패", e);
            // 썸네일 생성에 실패하더라도 비디오 정보는 저장할 수 있습니다.
            videoRepository.save(video);
        }
    }

    private String createThumbnailStoreFileName(String storeFileName) {
        if (storeFileName == null) {
            return "";
        }
        int pos = storeFileName.lastIndexOf(".");

        if (pos <= 0) {
            return storeFileName + "_thumb.jpg";
        }

        String baseName = storeFileName.substring(0, pos);
        return baseName + "_thumb.jpg";
    }
}
