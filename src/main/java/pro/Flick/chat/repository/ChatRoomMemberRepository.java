package pro.Flick.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.Flick.entity.ChatRoom;
import pro.Flick.entity.ChatRoomMember;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long>, ChatRoomMemberRepositoryCustom {

    /**
     * -- 1. member_id = 1이 참여하고 있는 모든 채팅방들의 목록을 구한다.
     * -- 2. 클라이언트가 id = 2인 채팅방 아이디를 누른다.
     * -- 3. CHAT_ROOM_MEMBER에서 id = 2에 해당하는 member_id들을 가져온다
     * -- 4. member 테이블과 조인하여 상대방의 프로필 정보를 가져온다
     *
     * select m.username, m.profile_image_uri, t.*
     * from member m
     * inner join
     * (select *
     * from chat_room_member
     * where chatroom_id in
     * (
     * select id
     * from chat_room
     * where id in (
     *   select chatroom_id from chat_room_member where member_id = 1
     * )
     * ) and member_id != 1) t
     * on m.id = t.member_id;
     *
     * @param memberId
     * @return
     */
    @Query("SELECT crm FROM ChatRoomMember crm JOIN FETCH crm.member m " +
            "WHERE crm.chatRoom IN (" +
            "    SELECT crm2.chatRoom FROM ChatRoomMember crm2 WHERE crm2.member.id = :memberId" +
            ") AND m.id != :memberId")
    List<ChatRoomMember> findChatRoomMembersWithMember(@Param("memberId") String memberId);


    /**
     * SELECT
     *     crm1.chatroom_id,
     *     crm1.member_id AS member_id_1,
     *     crm2.member_id AS member_id_2
     * FROM
     *     chat_room_member crm1
     * JOIN
     *     chat_room_member crm2 ON crm1.chatroom_id = crm2.chatroom_id
     * WHERE
     *     crm1.member_id < crm2.member_id;
     *
     *CHATROOM_ID 	MEMBER_ID_1  	MEMBER_ID_2
     1	1	2
     2	1	3
     (2 행, 1 ms)
     */

    /**
     * SELECT CHATROOM_ID
     * <p>
     * FROM CHAT_ROOM_MEMBER
     * <p>
     * WHERE MEMBER_ID IN (1, 2)
     * <p>
     * GROUP BY CHATROOM_ID
     * <p>
     * HAVING COUNT(DISTINCT MEMBER_ID) = 2;
     *
     * @Query("select m from Member m where m.username in :names")
     * List<Member> findByNames(@Param("names") Collection<String> names);
     */
    @Query("SELECT crm FROM ChatRoomMember crm WHERE crm.member.id in :memberIds GROUP BY crm.chatRoom.id HAVING COUNT(DISTINCT crm.member.id) = 2")
    Optional<ChatRoom> getChatRoomId(@Param("memberIds") List<Long> memberIds);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.id = (" +
            "  SELECT crm.chatRoom.id " +
            "  FROM ChatRoomMember crm " +
            "  WHERE crm.member.id IN :memberIds " +
            "  GROUP BY crm.chatRoom.id " +
            "  HAVING COUNT(DISTINCT crm.member.id) = :memberCount" +
            ")")
    Optional<ChatRoom> findChatRoomByMemberIds(
            @Param("memberIds") List<Long> memberIds,
            @Param("memberCount") int memberCount
    );
}

