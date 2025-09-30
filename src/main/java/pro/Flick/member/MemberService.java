package pro.Flick.member;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pro.Flick.Video.repository.VideoRepository;
import pro.Flick.Video.trash.dto.VideoWithMemberDto;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.controller.dto.SignUpDto;
import pro.Flick.member.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.follow.repository.FollowerRepository;
import pro.Flick.likes.repository.LikesRepository;
import pro.Flick.member.dto.*;
import pro.Flick.member.entity.MemberRole;
import pro.Flick.member.repository.MemberRepository;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {


    private final VideoRepository videoRepository;
    private final LikesRepository likesRepository;
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;
    private final FollowerRepository followerRepository;

    /*

     */

    public GetMemberByIdResponseDto getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        return new GetMemberByIdResponseDto(member);
    }

    public List<FindMembersByUsernameResponseDto> getMembersByUsername(String username) {

        List<Member> members = memberRepository.findMembersByUsername(username);
        return members.stream().map(FindMembersByUsernameResponseDto::new).toList();
    }

    public Member signUp(String email, String username, String password) {
        Boolean isExist = memberRepository.existsByUsername(username);

        if (isExist) {
            return null;
        }

        Member member = new Member();
        member.setEmail(email);
        member.setUsername(username);
        member.setPassword(passwordEncoder.encode(password));

        memberRepository.save(member);

        return member;
    }

    @Transactional
    public Long signUpV2(SignUpDto dto) {
        Member member = Member.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .isLock(false)
                .role(MemberRole.USER)
                .email(dto.getEmail())
                .build();

        return memberRepository.save(member).getId();
    }

    public GetMemberByIdResponseDto saveMember(String username, String email, String password) {
        Member member = memberRepository.save(new Member(username, email, password));
        return new GetMemberByIdResponseDto(member);
    }

    public void Login(String email, String password) {
    }

    public GetMemberByIdResponseDto getMemberByEmailAndPassword(String email, String password) {
        Member member = memberRepository.findMemberByEmailAndPassword(email, password);
        return new GetMemberByIdResponseDto(member);
    }

    public List<GetMemberByIdResponseDto> getMembersByIds(List<Long> ids) {
        List<Member> members = memberRepository.findByIdIn(ids);
        return members.stream().map(GetMemberByIdResponseDto::new).toList();
    }

    /*

     */


    @Transactional
    public Page<VideoWithMemberDto> getVideosByMemberIdWithMemberPage(Pageable pageable, Long memberId) {
        Page<Video> videos = videoRepository.findAllVideosByMemberId(pageable, memberId);
        return videos.map(VideoWithMemberDto::new);
    }

    @Transactional
    public Page<LikedVideoResponseDTO> getLikedVideos(Pageable pageable, Long memberId) {
        return likesRepository.QfindLikedVideosByMemberId(pageable, memberId);
    }


    @Transactional
    public Long getLikesCountByMemberId(Long memberId) {
        return likesRepository.findLikesCountByMemberId(memberId);
    }

    @Transactional
    public ProfileInfoResponseDTO getMemberInfo(Long profileUserId, Long loggedInUserId) {
        FollowCountDTO followCountDTO = followerRepository.findFollowCountsByMemberId(profileUserId);
        ;
        Member member = memberRepository.findById(profileUserId).orElseThrow(EntityNotFoundException::new);

        Long totalLikes = videoRepository.QfindTotalLikes(profileUserId);

        Boolean amIFollowing = followerRepository.QfindAmIFollowing(loggedInUserId, profileUserId);

        log.info("followCountDTO={}", followCountDTO);

        return new ProfileInfoResponseDTO(member, followCountDTO.getFollowingCount(), followCountDTO.getFollowerCount(), totalLikes, amIFollowing);
    }


    /**
     * 상대방 프로필의 정보를 봤을 떄 내가 팔로우하는지 알아야 함
     */
    @Transactional
    public ProfileInfoResponseDTOV2 getMemberInfoV2(Long myId, Long profileId) {
        log.info("getMemberInfoV2 start");
//        return memberRepository.findMemberProfile(myId, profileId);
        return memberRepository.QfindMemberProfile(myId, profileId);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername = {}", username);

        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            // 이 예외는 스프링 시큐리티에 사용자가 없음을 알리는 표준 방식입니다.
            // 이 예외가 발생하면 서버는 500 에러 대신 401 Unauthorized 또는 403 Forbidden 응답을 보냅니다.
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        log.info("member={}", member.getUsername());
        log.info("member={}", member.getPassword());

        return new CustomUserDetails(member);
//        return User
//                .builder()
//                .username(username)
//                .password(member.getPassword())
//                .roles(member.getRole().name())
//                .accountLocked(member.getIsLock())
//                .build();
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
