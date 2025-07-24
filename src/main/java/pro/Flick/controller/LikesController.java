package pro.Flick.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.repsository.FileRepository;
import pro.Flick.repsository.LikesRepository;
import pro.Flick.repsository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;

    @PostMapping("/likes")
    public void addLikes(@RequestBody LikesRequestDto likesRequestDto) {
        Member findMember = memberRepository.findMemberById(likesRequestDto.getUserId());
        Video findVideo = fileRepository.findVideoById(likesRequestDto.getVideoId());

        likesRepository.addLike(findMember, findVideo);
    }

    @Data
    static class LikesRequestDto {
        private String userId;
        private String videoId;
        private String videoUserId;

        public LikesRequestDto(String userId, String videoId, String videoUserId) {
            this.userId = userId;
            this.videoId = videoId;
            this.videoUserId = videoUserId;
        }
    }
}
