package pro.Flick.controller;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.Video.repository.VideoRepository;
import pro.Flick.Video.service.VideoService;
import pro.Flick.advertisement.repository.AdvertisementRepository;
import pro.Flick.comment.CommentService;
import pro.Flick.entity.*;
import pro.Flick.chat.repository.ChatJpaRepository;
import pro.Flick.Video.trash.repository.VideoJpaRepository;
import pro.Flick.file.FileRepository;
import pro.Flick.member.MemberService;
import pro.Flick.member.entity.Member;
import pro.Flick.member.repository.MemberRepository;
import pro.Flick.chat.ChatService;
import pro.Flick.follow.FollowService;
import pro.Flick.notification.NotificationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbInit {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final VideoJpaRepository videoJpaRepository;
    private final FollowService followService;
    private final ChatJpaRepository chatJpaRepository;
    private final VideoService videoService;
    private final ChatService chatService;
    private final CommentService commentService;
    private final VideoRepository videoRepository;
    private final AdvertisementRepository advertisementRepository;
    private final FileRepository fileRepository;
    private final EntityManager em;
    private final NotificationService notificationService;

    @Transactional
    public void signUpTest() {
        Member Jason = memberService.signUp("Email", "jason", "123");
        Member Chloe = memberService.signUp("Email2", "chloe", "123");

        String[] manNames = {
                "liam", "noah", "jane", "sophia", "james", "william", "benjamin", "isabella", "luna", "theodore",
                "news", "levi", "army", "jackson", "hiroto", "haruto", "minato", "sota", "yuto", "kaito",
                "hana", "yui", "sakura", "rin", "nana", "mei", "akari", "saki", "yuna", "sora",

                "grayson", "michael", "ethan", "aiden", "jackson", "maverick", "isaac", "caleb", "leo", "jayden",
                "john", "nicholas", "dylan", "christopher", "landon", "andrew", "joshua", "nathan", "thomas", "ryan",
                "adrian", "asher", "connor", "eli", "gavin", "hunter", "isaiah", "jaxon", "kai", "lincoln",
                "milo", "nolan", "parker", "phoenix", "roman", "silas", "sterling", "tristan", "victor", "vincent",
                "wesley", "xavier", "zane", "adam", "arthur", "austin", "bentley", "brooks", "bryson", "caden",
                "colton", "cooper", "damian", "dawson", "dean", "dominic", "emmett", "eric", "felix", "finn",
                "forrest", "george", "graham", "harrison", "hayden", "ian", "ivan", "jace", "jacob"
        };


        List<Member> tempMembers = new ArrayList<Member>();

        for (int i = 0; i < 30; i++) {
            Member tempMember = memberService.signUp("email" + i + 3, manNames[i], "123");
            tempMembers.add(tempMember);
            File profileImage = new File(manNames[i] + "_profile.png", tempMember);
            fileRepository.save(profileImage);
            tempMember.setFile(profileImage);

            followService.memberAFollowsMemberBUsingRef(tempMember.getId(), Chloe.getId());
        }



        File jasonProfileImage = new File("jason_profile.png", Jason);
        File chloeProfileImage = new File("chloe_profile.png", Chloe);

        fileRepository.save(jasonProfileImage);
        fileRepository.save(chloeProfileImage);

        Jason.setFile(jasonProfileImage);
        Chloe.setFile(chloeProfileImage);

        Member Akari = memberService.getMemberByUsername("akari");
        Member Nana = memberService.getMemberByUsername("nana");

        followService.memberAFollowsMemberBUsingRef(Jason.getId(), Chloe.getId());
        followService.memberAFollowsMemberBUsingRef(Chloe.getId(), Jason.getId());

        em.flush();

        // 실시간으로 팔로워가 추가되는 데모를 위한 쓰레드
        new Thread(new Runnable() {
            @Override
            public void run() {
                Member Nana = null;

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                while ((Nana = memberService.getMemberByUsername("nana")) == null) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                for (int i = 0; i < 10; i++) {
                    try {
                        log.warn("notificationService.send to nana");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Member memberByUsername = memberService.getMemberByUsername(manNames[i]);
                    notificationService.send(Nana.getId(), memberByUsername.getId(), NotificationType.FOLLOW, "");
                }
            }
        }).start();


        Member Test3 = memberService.signUp("Email3", "Test3", "123");
        Member Test4 = memberService.signUp("Email3", "Test4", "123");


        Member Jackson = memberService.getMemberByUsername("Jackson");

        videoService.createVideo("カフェでひとり、でも", "nana_video_cafe.mp4", Nana);
        videoService.createVideo("カフェでひとり、でも", "jackson_soccer_video.mp4", Jackson);
        videoService.createVideo("시부야 불빛 아래 ✨", "akari_street_video.mp4", Akari);
        videoService.createVideo("myVideo1", "test.mov", Jason);
        videoService.createVideo("myVideo2","test2.mov", Jason);
        videoService.createVideo("myVideo3","test3.mov", Chloe);

//        for (int i = 0; i < 100; i++) {
//            chatService.addMessage(Jason, Chloe, i + "");
//        }
        chatService.addMessage(Jason, Chloe, "2");
        chatService.addMessage(Jason, Chloe, "3");
        chatService.addMessage(Chloe, Jason, "4");
        chatService.addMessage(Chloe, Jason, "5");
        chatService.addMessage(Chloe, Jason, "6");

        chatService.addMessage(Jason, Test3, "6");
        chatService.addMessage(Jason, Test3, "6");
        chatService.addMessage(Jason, Test3, "6");
    }

    @Transactional
    public void saveMemberAndVideo() {

        memberRepository.save(new Member("HelloWorld", "Email", "123"));
        Member test = memberRepository.save(new Member("test", "Email2", "123"));
        memberRepository.save(new Member("test3", "Email3", "123"));
        memberRepository.save(new Member("test4", "Email4", "123"));



        Member member = memberRepository.findMemberByEmailAndPassword("Email", "123");
        Member member2 = memberRepository.findMemberByEmailAndPassword("Email2", "123");
        Member member3 = memberRepository.findMemberByEmailAndPassword("Email3", "123");
        Member member4 = memberRepository.findMemberByEmailAndPassword("Email4", "123");

//        List<Member> memberList = new ArrayList<>();

//        save1000TestMembers(memberList);
//
//        for (Member m : memberList) {
//            followService.memberAFollowsMemberB(member, m);
//        }
//
//        memberRepository.saveAll(memberList);


        /*
            8/1
            아래와 같이 영상의 제목만 넘겨도 영상을 볼 수 있도록 수정해야 한다
            fileRepository.saveVideo("test")
         */


        Video AdVideoTest = Video.builder()
                .videoType(VideoType.AD)
                .uri("http://127.0.0.1:8080/videos-v2/download/ad_video_test.mp4")
                .title("광고 영상입니다.")
                .member(member)
                .build();


        File adFile = new File("Gemini_Generated_Image_qj36ovqj36ovqj36.png", null);
        fileRepository.save(adFile);

        advertisementRepository.save(new Advertisement("광고 제목입니다", "http://localhost:8080/ad-1", adFile, AdVideoTest));

        Video ShoppingVideoTest = Video.builder()
                .videoType(VideoType.SHOPPING)
                .uri("http://127.0.0.1:8080/videos-v2/download/test.mov")
                .title("쇼핑 영상입니다.")
                .member(member)
                .build();

        videoRepository.save(AdVideoTest);
        videoRepository.save(ShoppingVideoTest);

//        videoJpaRepository.saveVideo("myVideo", "http://127.0.0.1:8080/videos-v2/download/test.mov", member);
//        videoJpaRepository.saveVideo("myVideo2", "http://127.0.0.1:8080/videos-v2/download/test2.mov", member);
//        videoJpaRepository.saveVideo("myVideo3", "http://127.0.0.1:8080/videos-v2/download/test3.mov", member2);

        videoService.createVideo("myVideo1", "test.mov", member);
        videoService.createVideo("myVideo2","test2.mov", member);
        videoService.createVideo("myVideo3","test3.mov", test);
        videoService.createVideo("adVideo", "ad_video_test.mp4", null);

        followService.memberAFollowsMemberBUsingRef(member.getId(), member2.getId());
        followService.memberAFollowsMemberBUsingRef(member2.getId(), member.getId());


        commentService.addCommentV2(1L, 1L, "hello world1");
        commentService.addCommentV2(1L, 1L, "hello world2");
        commentService.addCommentV2(1L, 1L, "hello world3");
        commentService.addCommentV2(1L, 1L, "hello world4");
        commentService.addCommentV2(2L, 1L, "hello world5");

//        save100MembersVideos(memberList);

//        chatJpaRepository.addMessage(member, "1", "1:2");
//        chatJpaRepository.addMessage(member, "2", "1:2");
//        chatJpaRepository.addMessage(member, "3", "1:2");
//        chatJpaRepository.addMessage(member2, "4", "1:2");
//        chatJpaRepository.addMessage(member2, "5", "1:2");
//        chatJpaRepository.addMessage(member2, "6", "1:2");


        /*
            1. 팔로워 목록에서 누름
            2. 내 member_id, 상대방 member_id 전달
            3. 이렇게 둘이 참여하고 있는 채팅방이 있는지 검사
            4. 없다면 새로운 채팅방을 만든다
         */

        chatService.addMessage(member, member2, "1");
        chatService.addMessage(member, member2, "2");
        chatService.addMessage(member, member2, "3");
        chatService.addMessage(member2, member, "4");
        chatService.addMessage(member2, member, "5");
        chatService.addMessage(member2, member, "6");

        chatService.addMessage(member, member3, "6");
        chatService.addMessage(member, member3, "6");
        chatService.addMessage(member, member3, "6");
    }

    private void save100MembersVideos(List<Member> memberList) {
        for (Member m : memberList) {
            videoJpaRepository.saveVideo(m.getUsername() + "'s video", "http://127.0.0.1:8080/video/test3.mov", m);
        }
    }

    private static void save1000TestMembers(List<Member> memberList) {
        for (int i = 0; i < 100; i++) {
            memberList.add(new Member("user" + i, "sample@email.com" + i, "123"));
        }
    }
}
