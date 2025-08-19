package pro.Flick.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.Flick.repsository.LikesRepository;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;

    @Transactional
    public void addCommentLikes() {
    }
}
