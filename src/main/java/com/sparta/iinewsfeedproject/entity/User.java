package com.sparta.iinewsfeedproject.entity;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="user")
@NoArgsConstructor
public class User extends Timestamped  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "유저 고유번호")
    private Long id;
    @OneToMany(mappedBy = "fromUser")
    private List<Friend> friend = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Post> post = new ArrayList<>();

    @Column(name="name")
    @Comment(value = "이름")
    private String name;

    @Column(name="email")
    @Comment(value = "이메일")
    private String email;

    @Column(name="password")
    @Comment(value = "비밀번호")
    private String password;

    @Column(name="deletedAt")
    @Comment(value = "삭제일 - Null 유무로 탈퇴 체크")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;

    public void createUser(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, password.toCharArray());
    }

    public void updateName(String name){
        this.name = name;
    }
    public void updatePassword(String password) {
        this.password = BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, password.toCharArray());
    }

    public void deactivate() {
        this.deletedAt = LocalDateTime.now();
    }
}
