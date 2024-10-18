package com.sparta.iinewsfeedproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@Table(name="friend")
@NoArgsConstructor
public class Friend extends Timestamped  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "친구 고유번호")
    private Long id;

    @JoinColumn(name="from_user_id" , referencedColumnName = "id")
    @Comment(value = "요청 보내는 사람의 아이디(유저고유번호) - 유저 테이블과 연동")
    private Long from_user_id;
    @ManyToOne // 유저테이블과 연관관계 설정
    private User user;

    // [ to_id 튜터님이 ERD 리뷰하실때 마지막이 기억이 안나서 일단 주석처리 해뒀는데 나중에 필요하면 넣으면 될듯합니다.
    // @Column(name="to_id")
    // @Comment(value = "요청 받는 사람의 아이디")
    // private String to_id;

    @Column(name="friendRequest", nullable = false)
    @Comment(value = "친구요청 승인 여부 (Y : 승인 / N : 거절)")
    private String friendRequest;
}
