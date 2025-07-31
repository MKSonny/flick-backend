package pro.Flick.repsository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Chat;
import pro.Flick.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ChatRepository {

    @Autowired
    private EntityManager em;

    public List<Chat> getChatByUsersKey(String usersKey) {
        return em.createQuery("select c from Chat c where c.usersKey=:usersKey", Chat.class)
                .setParameter("usersKey", usersKey)
                .getResultList();
    }

    @Transactional
    public void addMessage(Member user_id, String text, String users_key) {
        em.persist(new Chat(users_key, user_id, text, LocalDateTime.now()));
    }
}
