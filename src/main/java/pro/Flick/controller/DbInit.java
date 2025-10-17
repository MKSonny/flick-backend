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
import java.util.Random;
import java.util.concurrent.CompletableFuture;

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
                        Thread.sleep(100);
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


        Member Jackson = memberService.getMemberByUsername("jackson");
        Member Yui = memberService.getMemberByUsername("yui");

//        videoService.createVideo("カフェでひとり、でも", "nana_video_cafe.mp4", Nana);
        CompletableFuture<Video> akari_street_video = videoService.createVideo("시부야 불빛 아래 ✨", "akari_street_video.mp4", Akari);
        CompletableFuture<Video> yui_tokyo_video = videoService.createVideo("도쿄의 밤, 빛이 참 예쁘다 \uD83C\uDF03✨", "yui_tokyo_video.mp4", Yui);
//        videoService.createVideo("피, 땀, 눈물의 결실… 드디어 우승컵을 든 한국!", "jackson_soccer_video.mp4", Jackson);
//        videoService.createVideo("myVideo1", "test.mov", Jason);
//        videoService.createVideo("myVideo2","test2.mov", Jason);
//        videoService.createVideo("myVideo3","test3.mov", Chloe);


        String[] commentTextForakari_street_video = {
                "시부야 밤감성 진짜 미쳤다",
                "이 조명 아래에서는 누가 찍어도 영화 같아",
                "Tokyo vibes 완전 찐이다",
                "와 진짜 현실 일본 감성 그 자체다",
                "カッコいい！夜の渋谷ってやっぱ最高〜",
                "이건 그냥 시부야가 사람을 예쁘게 만드는 거임",
                "분위기 진짜 예술 조명 반사되는 얼굴 미쳤다",
                "이거 어디서 찍은 거예요 완전 가보고 싶어요",
                "도쿄의 불빛이 이렇게 따뜻할 줄은 몰랐음",
                "그냥 걷고 있는데도 뮤직비디오 같아",
                "와 셀카인데 이 정도 퀄리티면 드라마 가능",
                "渋谷のネオンライトが最高に綺麗",
                "너무 자연스러워서 진짜 여행 브이로그인 줄",
                "이거 보고 일본 가고 싶어졌어요",
                "카메라 필터 안 쓴 거 맞죠 색감 대박이다",
                "진짜 일본의 밤은 이런 느낌이구나",
                "Tokyo street light magic",
                "영상 전체가 따뜻한 공기 같아요",
                "도쿄 밤거리에서 이렇게 평화로울 수가 있나",
                "BGM이랑 너무 잘 어울린다"
        };

        String[] commentTextForYui_tokyo_video = {
                "도쿄 야경 진짜 아름답다",
                "이 영상 보니까 당장 여행 가고 싶다",
                "조명과 배경이 완전 영화 장면 같아요",
                "셀카인데 퀄리티 미쳤다",
                "바람에 머리 흔들리는 느낌까지 섬세하네",
                "영상 전체가 평화로운 브이로그 느낌",
                "도쿄 밤거리 감성 제대로 담긴 영상",
                "와 색감 너무 예쁘다 현실감 장난 아님",
                "이 분위기 진짜 최고",
                "조명 반사되는 느낌이 영화 같아요",
                "영상 보고 힐링된다",
                "도쿄 밤거리 걷고 있는 기분",
                "자연스러움이 너무 좋다",
                "와 진짜 여행 가고 싶어짐",
                "この雰囲気最高だね",
                "영상 속 풍경 너무 아름다워",
                "도쿄의 밤, 이렇게 예쁠 수 있나",
                "이 영상 보니까 카메라 사고 싶다",
                "진짜 브이로그 감성 장난 아님",
                "도시 불빛과 얼굴 조화가 예술이다",
                "와 분위기 진짜 영화 같아요",
                "이건 그냥 현실 일본 감성 그 자체",
                "배경 사람들까지 자연스럽게 살아있네",
                "영상에서 나오는 거리 느낌 너무 좋다",
                "도쿄 야경 보면서 힐링한다",
                "이거 브이로그 팁 좀 알려주세요",
                "色合いと光のバランスが素敵",
                "영상 보는 내내 눈이 즐겁다",
                "도쿄에서 이런 영상 찍고 싶다",
                "조명 반사와 얼굴 표현이 대박",
                "이거 보고 나도 셀카 찍고 싶음",
                "영상 하나로 여행 온 느낌이다",
                "도쿄 밤 느낌 제대로 살렸네요",
                "영상 속 조명 표현이 너무 자연스럽다",
                "걸으면서 찍은 것 같아 더 자연스럽다",
                "진짜 현실감 넘치는 브이로그다",
                "도쿄 여행 감성 제대로 담겼다",
                "夜景とセルフィーが完璧にマッチしてる",
                "영상 보는 내내 힐링된다",
                "東京の夜景って本当に綺麗",
                "この映像見てると旅行したくなる",
                "ネオンライトの反射が美しい",
                "東京に行きたくなる動画だね",
                "夜の街の雰囲気が最高",
                "夜景とセルフィーのバランスがいい",
                "静かで落ち着いた雰囲気",
                "街の灯りと笑顔が素敵",
                "日本の夜景ってやっぱ最高だね",
                "この映像だけで旅行気分になる",
                "東京の夜の散歩気分",
                "映像の色味が素晴らしい",
                "自然な表情がいい感じ",
                "動画見てるだけでワクワクする",
                "東京の夜を感じられる動画"
        };

        String[] replies = {
                "진짜 그 말 공감돼요 영상 분위기 완전 일본 같아요",
                "맞아요 색감이랑 조명 느낌이 완전 도쿄 거리 감성",
                "이건 일본 사람 아니면 못 내는 분위기임",
                "조명 반사되는 게 영화 한 장면 같죠"
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                akari_street_video.thenApply(video -> {
                    Comment forReplyComment = null;

                    for (int i = 0; i < 20; i++) {
                        Member tempM = memberService.getMemberByUsername(manNames[i]);
                        Comment comment = commentService.addCommentV2ForTest(tempM.getId(), video.getId(), commentTextForakari_street_video[i]);

                        if (i == 3) {
                            forReplyComment = comment;
                        }
                    }

                    return video;
                });
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {

                yui_tokyo_video.thenApply(video -> {

                    for (int i = 0; i < 20; i++) {
                        Member tempM = memberService.getMemberByUsername(manNames[i]);
                        log.info("{}", i);
                        log.info("member {} added comment = {}", manNames[i], commentTextForYui_tokyo_video[i]);
                        commentService.addCommentV2ForTest(tempM.getId(), video.getId(), commentTextForYui_tokyo_video[i]);
                    }

                    return video;
                });
            }
        }).start();


        yui_tokyo_video.thenApply(video -> {
            video.setLikesCount(327193L);
            video.setCommentCount(342L);
            videoRepository.save(video);
            return video;
        });


        akari_street_video.thenApply(video -> {
            video.setLikesCount(123423L);
            video.setCommentCount(232L);
            videoRepository.save(video);
            return video;
        });


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
