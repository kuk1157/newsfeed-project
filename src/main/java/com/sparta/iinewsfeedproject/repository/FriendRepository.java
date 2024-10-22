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
    String query = "SELECT COUNT(*) FROM friend WHERE to_user_id = :toUserId AND status != 'REJECT' ";
    @Query(value = query, nativeQuery = true)
    int findByToUserIdAndStatus(Long toUserId);

    // 친구요청이 아예 없을 경우 체크
    String queryAll = "SELECT COUNT(*) FROM friend";
    @Query(value = queryAll, nativeQuery = true)
    int findAllById();

    // 친구 요청 상태값만 조회
    String queryStatus = "SELECT status FROM friend WHERE id = :id";
    @Query(value = queryStatus, nativeQuery = true)
    String findAllByStatus(Long id);

    List<Friend> findByStatus(String status);

    // 본인이 받은 친구 요청 목록만 확인하는 용도 - 추후가공해서 사용예정
    // List<Friend> findByToUserIdAndStatus(User toUserId, String status);
}
