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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fromUserId" , referencedColumnName = "id", updatable=false)
    @Comment(value = "요청 보내는 사람의 아이디(유저고유번호) - 유저 테이블과 연동")
    private User fromUser;

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
