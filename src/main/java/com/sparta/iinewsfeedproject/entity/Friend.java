package com.sparta.iinewsfeedproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name="friend")
@NoArgsConstructor
public class Friend extends Timestamped  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "친구 고유번호")
    private Long id;

    // 기본값이 EAGER 즉시 로딩이라 N + 1 문제가 발생할 수도 있기 때문에
    // FetchType.Lazy는 지연로딩을 하기 위해 달아주었습니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fromUserId" , referencedColumnName = "id", updatable=false)
    @Comment(value = "요청 보내는 사람의 아이디(유저고유번호) - 유저 테이블과 연동")
    private User fromUser;

    // User 매핑을 두개나 해둘 필요 없이 user_id는 토큰에서 꺼내어서
    // 값을 넣어주면 될 것 같습니다.
    @Column(name="toUserId", updatable=false)
    @Comment(value = "요청 받는 사람의 아이디")
    private Long toUserId;

    @Column(name="status", nullable = false)
    @Comment(value = "친구요청 승인 여부 (PENDING : 대기(기본값), ACCEPT : 승인 / REJECT : 거절)")
    private String status;

    public Friend(Long toUserId, String status, User fromUser) {
        this.toUserId = toUserId;
        this.status = status;
        this.fromUser = fromUser;
    }

    public void update(String status ) {
        this.status = status;
    }

}
