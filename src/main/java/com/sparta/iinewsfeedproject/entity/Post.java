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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id" ,referencedColumnName = "id")
    private User user;

    @Column(name="content", nullable = false, length=255)
    @Comment(value = "게시물 내용")
    private String content;

    public Post(String content, User user) {
        this.content = content;
        this.user = user;
    }

    public void modifyContent(String content) {
        this.content = content;
    }
}
