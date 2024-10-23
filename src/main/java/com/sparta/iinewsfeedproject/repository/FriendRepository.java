package com.sparta.iinewsfeedproject.repository;

import com.sparta.iinewsfeedproject.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    // 친구 관계가 존재하는지 확인
    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long userId);

    // 특정 친구 관계 삭제
    void deleteByFromUserIdAndToUserId(Long fromUserId, Long userId);

    // 특정 사용자의 친구 목록 중 수락된 친구만 조회
    List<Friend> findByFromUserIdAndStatus(Long fromUserId, String status);

    // 요청받은 유저가 fromUserId에 존재할 경우 삭제
    void deleteByFromUserId(Long fromUserId);

    // 요청받은 유저가 toUserId에 존재할 경우 삭제
    void deleteByToUserId(Long toUserId);

    // 요청받는 사람의 승인,대기값 데이터의 개수 구하기
    String query = "SELECT COUNT(id) FROM friend WHERE to_user_id = :toUserId AND from_user_id = :fromUserId AND status != 'REJECT' ";
    @Query(value = query, nativeQuery = true)
    int findByToUserIdAndStatus(Long toUserId, Long fromUserId);

    // 친구요청이 아예 없을 경우 체크
    String queryAll = "SELECT COUNT(id) FROM friend";
    @Query(value = queryAll, nativeQuery = true)
    int findAllById();

    // 중복체크 - 로그인한 회원 id와 요청보내는 id를 비교해서 존재하는지
    String friendId = "SELECT COUNT(id) FROM friend WHERE to_user_id = :fromUserId AND from_user_id = :toUserId";
    @Query(value = friendId, nativeQuery = true)
    int findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    // 친구 요청 상태값만 조회
    String queryStatus = "SELECT status FROM friend WHERE id = :id";
    @Query(value = queryStatus, nativeQuery = true)
    String findAllByStatus(Long id);

    // 본인이 받은 친구 요청 목록만 수락&거절 할 수 있게
    String response = "SELECT COUNT(id) FROM friend WHERE to_user_id = :toUserId AND status = :status AND id = :id";
    @Query(value = response, nativeQuery = true)
    int findByToUserIdAndStatusAndId(Long toUserId, String status, Long id);

    // 본인이 받은 친구 요청 목록만 조회
     List<Friend> findByToUserIdOrFromUserIdAndStatus(Long toUserId, Long fromUserId, String status);
}
