package pro.Flick.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.Flick.chat.ChatRoomMemberResponseDTO;
import pro.Flick.chat.GetChatByUsersKeyResponseDtoV2;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Chat;
import pro.Flick.entity.ChatRoomMember;
import pro.Flick.entity.Member;
import pro.Flick.repsository.ChatJpaRepository;
import pro.Flick.repsository.ChatRoomMemberRepository;
import pro.Flick.member.MemberJpaRepository;
import pro.Flick.service.ChatService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatJpaRepository chatJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatService chatService;

    // 8/1 채팅 기능 수정
//    @PostMapping
//    public void addMessage(@RequestBody ChatRequestDto requestDto) {
//        Member findMember = memberJpaRepository.findMemberById(requestDto.getUserId());
//        chatJpaRepository.addMessage(findMember, requestDto.getText(), requestDto.getUsers_key());
//    }
    // 8/1

    @GetMapping("/{users_key}")
    public List<GetChatByUsersKeyResponseDto> getChatByUsersKey(@PathVariable String users_key) {
        List<Chat> chats = chatJpaRepository.getChatByUsersKey(users_key);
        List<GetChatByUsersKeyResponseDto> dtoList = new ArrayList<>();

        for (Chat chat : chats) {
            dtoList.add(new GetChatByUsersKeyResponseDto(chat.getMember(), chat.getText(), chat.getLocalDateTime()));
        }
        return dtoList;
    }

    @GetMapping("/v2/{chatRoomId}")
    public List<GetChatByUsersKeyResponseDtoV2> getChatsByChatRoomId(@PathVariable Long chatRoomId) {
        return chatService.getChatMessages(chatRoomId);
    }

    @GetMapping("/my_chats/{userId}")
    public List<ChatRoomMemberResponseDTO> getMyChats(@PathVariable String userId) {
        log.info("hello world={}", userId);
        List<ChatRoomMember> chatRoomMembersWithMember = chatRoomMemberRepository.findChatRoomMembersWithMember(userId);
        return chatRoomMembersWithMember.stream().map(ChatRoomMemberResponseDTO::new).toList();
    }


    /**
     * userId: user.id,
     * chat_user_id: params.chat_user_id,
     * text,
     * users_key
     */
//    @PostMapping
    public void addMessage(@RequestBody ChatRequestDto requestDto) {
        Member findMember = memberJpaRepository.findMemberById(requestDto.getUserId());
        Member receiverMember = memberJpaRepository.findMemberById(requestDto.getChat_user_id());
        chatJpaRepository.addMessage(findMember, requestDto.getText(), requestDto.getUsers_key());

        chatJpaRepository.addMessage(findMember, receiverMember, requestDto.getText());
    }

    @PostMapping
    public void addMessageV2(@RequestBody ChatRequestDto requestDto) {
        Member sender = memberJpaRepository.findMemberById(requestDto.getUserId());
        log.info("requestDto={}", requestDto);
        Member receiver = memberJpaRepository.findMemberById(requestDto.getChat_user_id());

        chatService.addMessage(sender, receiver, requestDto.getText());
    }

    public void findMyChats() {

    }

    @Data
    static class ChatRequestDto {
        private String userId; // 보낸 사람의 id
        private String chat_user_id;
        private String text;
        private String users_key;

        public ChatRequestDto(String userId, String chat_user_id, String text, String users_key) {
            this.userId = userId;
            this.chat_user_id = chat_user_id;
            this.text = text;
            this.users_key = users_key;
        }
    }

    @Data
    static class GetChatByUsersKeyResponseDto {
        private GetMemberByIdResponseDto user;
        private String text;
        private LocalDateTime time;

        public GetChatByUsersKeyResponseDto(Member member, String text, LocalDateTime time) {
            this.user = new GetMemberByIdResponseDto(member);
            this.text = text;
            this.time = time;
        }
    }
}
