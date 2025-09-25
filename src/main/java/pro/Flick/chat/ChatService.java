package pro.Flick.chat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.Flick.api_response.BusinessException;
import pro.Flick.api_response.ErrorCode;
import pro.Flick.chat.dto.ChatRequestDTO;
import pro.Flick.chat.dto.ChatRoomInfoResponseDTO;
import pro.Flick.chat.dto.ChatRoomMemberCountResponseDto;
import pro.Flick.chat.dto.GetMessagesResponseDto;
import pro.Flick.chat.repository.ChatRoomMemberRepository;
import pro.Flick.chat.repository.ChatRoomRepository;
import pro.Flick.chat.repository.MemberMessageStatusRepository;
import pro.Flick.entity.*;
import pro.Flick.member.entity.Member;
import pro.Flick.member.repository.MemberRepository;
import pro.Flick.chat.repository.MessageRepository;

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
    private final MemberMessageStatusRepository memberMessageStatusRepository;

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

        Member sender = memberRepository.findById(senderId).orElseThrow(() -> new BusinessException(ErrorCode.NO_DATA_FOUND, "해당 멤버를 찾을 수 없음"));
        Member receiver = memberRepository.getReferenceById(receiverId);

        String text = chatRequestDTO.getText();

        log.info("senderId={}, receiverId={}", senderId, receiverId);


        ChatRoom chatRoom = null;

        if (chatRequestDTO.getChatRoomId() != null) {
            chatRoom = chatRoomRepository.findById(chatRequestDTO.getChatRoomId()).orElseThrow();
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

        memberMessageStatusRepository.save(new MemberMessageStatus(message, sender, chatRoom, MessageStatus.READ, message.getCreatedAt()));
        memberMessageStatusRepository.save(new MemberMessageStatus(message, receiver, chatRoom, MessageStatus.DELIVERED, null));


        Message savedMessage = messageRepository.save(message);
        chatRoom.setLastMessage(savedMessage);

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

        memberMessageStatusRepository.save(new MemberMessageStatus(savedMessage, sender, chatRoom, MessageStatus.READ, message.getCreatedAt()));
        memberMessageStatusRepository.save(new MemberMessageStatus(savedMessage, receiver, chatRoom, MessageStatus.DELIVERED, null));

        chatRoom.setLastMessage(savedMessage);
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
        return chatRoomMemberRepository.QgetMyChatsV2(pageable, memberId);
    }

    @Transactional
    public Page<ChatRoomMemberCountResponseDto> getMyChatListCount(Pageable pageable, Long memberId) {
        return memberMessageStatusRepository.QGetMyChatsCountV2(pageable, memberId);
//        return chatRoomMemberRepository.QGetMyChatsCount(pageable, memberId);
    }


    public List<GetChatByUsersKeyResponseDtoV2> getChatMessages(Long chatRoomId) {
        List<Message> messages = messageRepository.findByChatRoomId(chatRoomId);
        List<GetChatByUsersKeyResponseDtoV2> dtoList = new ArrayList<>();
        for (Message message : messages) {
            dtoList.add(new GetChatByUsersKeyResponseDtoV2(message));
        }
        return dtoList;
    }

    public Page<GetMessagesResponseDto> getMessages(Pageable pageable, Long chatRoomId, Long memberId) {
        return memberMessageStatusRepository.QfindMessages(pageable, chatRoomId, memberId);
    }

    public ChatRoomInfoResponseDTO getRoomInfo(Long chatRoomId, Long memberId) {
        return chatRoomMemberRepository.QgetChatRoomInfo(chatRoomId, memberId);
    }

    @Transactional
    public void markMessagesAsRead(List<Long> messageIds, Long memberId) {
//        messageRepository.QmarkMessagesAsReadByIds(messageIds);
        messageRepository.QmarkMessagesAsReadByIdsV2(messageIds, memberId);
    }
}
