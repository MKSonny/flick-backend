package pro.Flick.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Chat;
import pro.Flick.entity.Member;
import pro.Flick.repsository.ChatRepository;
import pro.Flick.repsository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/{users_key}")
    public List<GetChatByUsersKeyResponseDto> getChatByUsersKey(@PathVariable String users_key) {
        List<Chat> chats = chatRepository.getChatByUsersKey(users_key);
        List<GetChatByUsersKeyResponseDto> dtoList = new ArrayList<>();

        for (Chat chat : chats) {
            dtoList.add(new GetChatByUsersKeyResponseDto(chat.getMember(), chat.getText(), chat.getLocalDateTime()));
        }
        return dtoList;
    }


    /**
     * userId: user.id,
     * chat_user_id: params.chat_user_id,
     * text,
     * users_key
     */
    @PostMapping
    public void addMessage(@RequestBody ChatRequestDto requestDto) {
        Member findMember = memberRepository.findMemberById(requestDto.getUserId());
        chatRepository.addMessage(findMember, requestDto.getText(), requestDto.getUsers_key());
    }

    @Data
    static class ChatRequestDto {
        private String userId;
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
