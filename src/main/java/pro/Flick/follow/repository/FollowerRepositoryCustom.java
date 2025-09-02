package pro.Flick.follow.repository;


public interface FollowerRepositoryCustom {

    Boolean QfindAmIFollowing(Long myId, Long profileUserId);
}
