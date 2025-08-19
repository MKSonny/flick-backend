package pro.Flick.member;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.Flick.Video.VideoRepository;
import pro.Flick.Video.dto.VideoWithMemberDto;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.repsository.FollowRepository;
import pro.Flick.repsository.LikesRepository;
import pro.Flick.repsository.MemberRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final VideoRepository videoRepository;
    private final LikesRepository likesRepository;

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;


    @Transactional
    public Page<VideoWithMemberDto> getVideosByMemberIdWithMemberPage(Pageable pageable, Long memberId) {
        Page<Video> videos = videoRepository.findAllVideosByMemberId(pageable, memberId);
        return videos.map(VideoWithMemberDto::new);
    }


    @Transactional
    public Long getLikesCountByMemberId(Long memberId) {
        return likesRepository.findLikesCountByMemberId(memberId);
    }

    @Transactional
    public ProfileInfoResponseDTO getMemberInfo(Long memberId) {
        FollowCountDTO followCountDTO = followRepository.findFollowCountsByMemberId(memberId);
        ;
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

        return new ProfileInfoResponseDTO(member, followCountDTO.getFollowingCount(), followCountDTO.getFollowerCount());
    }
}
