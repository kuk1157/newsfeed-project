package com.sparta.iinewsfeedproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@Table(name="post")
@NoArgsConstructor
public class Post extends Timestamped  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "뉴스피드 게시물 고유번호")
    private Long id;

    @JoinColumn(name="user_id" , referencedColumnName = "id")
    @Comment(value = "유저 고유번호 - 유저 테이블과 연동")
    private Long user_id;
    @ManyToOne // 유저테이블과 연관관계 설정
    private User user;

    @Column(name="content", nullable = false, length=255)
    @Comment(value = "게시물 내용")
    private String content;

}
