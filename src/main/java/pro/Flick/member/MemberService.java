package pro.Flick.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.Flick.Video.VideoRepository;
import pro.Flick.Video.dto.VideoWithMemberDto;
import pro.Flick.entity.Video;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final VideoRepository videoRepository;


    public List<VideoWithMemberDto> findVideosByMemberIdWithMember(Long memberId) {
        List<Video> videos = videoRepository.findVideosByMemberIdWithMember(memberId);
        return videos.stream().map(VideoWithMemberDto::new).toList();
    }
}
