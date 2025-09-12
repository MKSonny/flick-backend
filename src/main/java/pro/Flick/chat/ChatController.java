package pro.Flick.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import pro.Flick.api_response.ApiResponse;
import pro.Flick.chat.dto.ChatRequestDTO;
import pro.Flick.chat.dto.ChatRoomInfoResponseDTO;
import pro.Flick.chat.dto.ChatRoomMemberCountResponseDto;
import pro.Flick.chat.dto.ReadMessagesRequestDto;
import pro.Flick.chat.repository.ChatJpaRepository;
import pro.Flick.chat.repository.ChatRoomMemberRepository;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Chat;
import pro.Flick.entity.Member;
import pro.Flick.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatJpaRepository chatJpaRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatService chatService;

    // 8/1 채팅 기능 수정
//    @PostMapping
//    public void addMessage(@RequestBody ChatRequestDto requestDto) {
//        Member findMember = memberJpaRepository.findMemberById(requestDto.getUserId());
//        chatJpaRepository.addMessage(findMember, requestDto.getText(), requestDto.getChatRoomId());
//    }
    // 8/1

    @GetMapping("/{chatRoomId}")
    public List<GetChatByUsersKeyResponseDto> getChatByUsersKey(@PathVariable String users_key) {
        List<Chat> chats = chatJpaRepository.getChatByUsersKey(users_key);
        List<GetChatByUsersKeyResponseDto> dtoList = new ArrayList<>();

        for (Chat chat : chats) {
            dtoList.add(new GetChatByUsersKeyResponseDto(chat.getMember(), chat.getText(), chat.getLocalDateTime()));
        }
        return dtoList;
    }

    @GetMapping("/room-info/{chatRoomId}/{userId}")
    public ApiResponse<ChatRoomInfoResponseDTO> getChatRoomInfo(@PathVariable Long chatRoomId, @PathVariable Long userId) {
        return ApiResponse.ok(chatService.getRoomInfo(chatRoomId, userId));
    }

    @GetMapping("/v2/{chatRoomId}")
    public List<GetChatByUsersKeyResponseDtoV2> getChatsByChatRoomId(@PathVariable Long chatRoomId) {
        return chatService.getChatMessages(chatRoomId);
    }

    @GetMapping("/my_chats/{userId}")
    public Page<ChatRoomMemberResponseDTO> getMyChats(@PageableDefault(size = 5) Pageable pageable, @PathVariable Long userId) {
//        List<ChatRoomMember> chatRoomMembersWithMember = chatRoomMemberRepository.findChatRoomMembersWithMember(userId);
//        return chatRoomMembersWithMember.stream().map(ChatRoomMemberResponseDTO::new).toList();

        return chatService.getMyChatList(pageable, userId);
    }

    @GetMapping("/my_chats_count/{userId}")
    public Page<ChatRoomMemberCountResponseDto> getMyChatsCount(@PageableDefault(size = 5) Pageable pageable, @PathVariable Long userId) {
        return chatService.getMyChatListCount(pageable, userId);
    }

    @PostMapping("/messages/read")
    public ApiResponse<Void> markMessagesAsRead(@RequestBody ReadMessagesRequestDto request) {
//        chatService.markMessagesAsRead(request.messageIds());
        chatService.markMessagesAsRead(request.messageIds(), request.userId());
        return ApiResponse.ok(null);
    }


    /**
     * userId: user.id,
     * chat_user_id: params.chat_user_id,
     * text,
     * chatRoomId
     */
//    @PostMapping
    public void addMessage(@RequestBody ChatRequestDTO requestDto) {
        Member findMember = memberRepository.findMemberById(requestDto.getSenderId());
        Member receiverMember = memberRepository.findMemberById(requestDto.getReceiverId());
//        chatJpaRepository.addMessage(findMember, requestDto.getText(), requestDto.getChatRoomId());

        chatJpaRepository.addMessage(findMember, receiverMember, requestDto.getText());
    }

    @PostMapping
    public ApiResponse<?> addMessageV2(@RequestBody ChatRequestDTO requestDto) {
        Member sender = memberRepository.findMemberById(requestDto.getSenderId());
        log.info("requestDto={}", requestDto);
        Member receiver = memberRepository.findMemberById(requestDto.getReceiverId());


        return ApiResponse.ok(chatService.addMessage(requestDto));
    }

    @MessageMapping("/chat/{chatRoomId}/sendMessage")
    @SendTo("/topic/chat/{chatRoomId}")
    public ApiResponse<GetChatByUsersKeyResponseDtoV2> sendMessage(
            @DestinationVariable String chatRoomId,
            ChatRequestDTO messageRequest
    ) {

        log.info("ChatRequestDTO={}", messageRequest);

        return ApiResponse.ok(chatService.addMessage(messageRequest));
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
