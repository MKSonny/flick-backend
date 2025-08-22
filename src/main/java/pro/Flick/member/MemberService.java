package pro.Flick.member;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;
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
        log.info("hello world");
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

        log.info("hello world = {}", member.getProfileImageUri());

        return new ProfileInfoResponseDTO(member, followCountDTO.getFollowingCount(), followCountDTO.getFollowerCount());
    }



    /**
     * 상대방 프로필의 정보를 봤을 떄 내가 팔로우하는지 알아야 함
     */
    @Transactional
    public ProfileInfoResponseDTOV2 getMemberInfoV2(Long myId, Long profileId) {
        log.info("getMemberInfoV2 start");
        return memberRepository.findMemberProfile(myId, profileId);
    }

    /*
        {
          "id": "user123", // 사용자 고유 ID
          "username": "tiktok_user", // 사용자 이름
          "profileImageUrl": "http://...", // 프로필 사진 URL
          "isFollowedByMe": true // 내가 이 사람을 팔로우하고 있는지 여부
        }
     */
    @Transactional
    public Page<Temp> getFollowersByMemberId(Pageable pageable, Long memberId) {
        Page<Member> members = memberRepository.findFollowersByMemberId(pageable, memberId);
        return members.map(Temp::new);
    }

    @Transactional
    public Page<FollowerInfoDTO> getFollowersByMemberIdV2(Pageable pageable, Long memberId) {
        return memberRepository.findFollowersByMemberIdV2(pageable, memberId);
    }

    @Transactional
    public Page<FollowerInfoDTOV2> getFollowersByMemberIdV3(Pageable pageable, Long memberId) {
        return memberRepository.findFollowerByMemberIdV3(pageable, memberId);
    }

    @Data
    static class Temp {
        private Long id;
        private String username;
        private String profileImageUrl;
        private Boolean isFollowedByMe;

        public Temp(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
//            this.profileImageUrl = member.getProfileImageUri();
            this.profileImageUrl = member.getFile().getStoredFileName();
            this.isFollowedByMe = false;
        }
    }
}
