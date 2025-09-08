package pro.Flick.chat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.Flick.chat.dto.ChatRequestDTO;
import pro.Flick.chat.repository.ChatRoomMemberRepository;
import pro.Flick.chat.repository.ChatRoomRepository;
import pro.Flick.entity.ChatRoom;
import pro.Flick.entity.ChatRoomMember;
import pro.Flick.entity.Member;
import pro.Flick.entity.Message;
import pro.Flick.member.repository.MemberRepository;
import pro.Flick.repsository.MessageRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    /**
     * 1. 친구 목록에서 친구를 선택할 경우
     * 이미 채팅을 하고 있었는지 없었는지 구분할 수 있어야한다
     *
     * 1.1. 검사 로직
     *
     * 2. if 채팅을 진행한적 없다면 새로운 채팅방을 만들어야 한다
     *
     *
     *
     */
    @Transactional
    public GetChatByUsersKeyResponseDtoV2 addMessage(ChatRequestDTO chatRequestDTO) {

        Long senderId = Long.valueOf(chatRequestDTO.getSenderId());
        Long receiverId = Long.valueOf(chatRequestDTO.getReceiverId());

        Member sender = memberRepository.findById(senderId).orElseThrow();
        Member receiver = memberRepository.getReferenceById(receiverId);

        String text = chatRequestDTO.getText();

        log.info("senderId={}, receiverId={}", senderId, receiverId);


        ChatRoom chatRoom = null;

        if (chatRequestDTO.getChatRoomId() != null) {
            chatRoom = chatRoomRepository.getReferenceById(chatRequestDTO.getChatRoomId());
        } else {
            chatRoom = chatRoomMemberRepository.findChatRoomByMemberIds(List.of(sender.getId(), receiver.getId()), 2).orElseGet(() -> {
                System.out.println("채팅방이 없으므로 새로 생성합니다.");
                return createChatRoomAndChatRoomMessage(sender, receiver);
            });
        }


        Message message = Message.builder()
                .chatRoom(chatRoom)
                .read(false)
                .sender(sender)
                .createdAt(LocalDateTime.now())
                .text(text)
                .build();

        Message savedMessage = messageRepository.save(message);

        return new GetChatByUsersKeyResponseDtoV2(savedMessage);
    }

    @Transactional
    public void addMessage(Member sender, Member receiver, String text) {


        ChatRoom chatRoom = chatRoomMemberRepository.findChatRoomByMemberIds(List.of(sender.getId(), receiver.getId()), 2).orElseGet(() -> {
            System.out.println("채팅방이 없으므로 새로 생성합니다.");
            return createChatRoomAndChatRoomMessage(sender, receiver);
        });


        LocalDateTime nowTime = LocalDateTime.now();

//        chatRoomMemberRepository.updateLastReadAt(nowTime, chatRoom.getId());

        Message message = Message.builder()
                .chatRoom(chatRoom)
                .read(false)
                .sender(sender)
                .createdAt(nowTime)
                .text(text)
                .build();

        Message savedMessage = messageRepository.save(message);

        chatRoom.setMessage(savedMessage);
//        chatRoomRepository.updateChatRoomMessageId(message.getId(), chatRoom.getId());
    }

    @Transactional
    public ChatRoom createChatRoomAndChatRoomMessage(Member sender, Member receiver) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name("test")
                .createdAt(LocalDateTime.now())
                .build();

        chatRoomRepository.save(chatRoom);

        ChatRoomMember addSenderChatRoomMember = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .lastReadAt(chatRoom.getCreatedAt())
                .member(sender)
                .build();

        ChatRoomMember addReceiverChatRoomMember = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .lastReadAt(chatRoom.getCreatedAt())
                .member(receiver)
                .build();

        chatRoomMemberRepository.save(addSenderChatRoomMember);
        chatRoomMemberRepository.save(addReceiverChatRoomMember);

        return chatRoom;
    }

    @Transactional
    public Page<ChatRoomMemberResponseDTO> getMyChatList(Pageable pageable, Long memberId) {
//        return chatRoomMemberRepository.QgetMyChats(pageable, memberId);
        return chatRoomMemberRepository.QgetMyChatsV2(pageable, memberId);
    }


    public List<GetChatByUsersKeyResponseDtoV2> getChatMessages(Long chatRoomId) {
        List<Message> messages = messageRepository.findByChatRoomId(chatRoomId);
        List<GetChatByUsersKeyResponseDtoV2> dtoList = new ArrayList<>();
        for (Message message : messages) {
            dtoList.add(new GetChatByUsersKeyResponseDtoV2(message));
        }
        return dtoList;
    }
}
