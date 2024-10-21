package com.sparta.iinewsfeedproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="user")
@NoArgsConstructor
// Setter 사용은 지양하는게 좋을 것 같습니다.
// update 시에도 set 메서드를 이용하는 것 보다 modify 나 update 메서드를 만들어서 사용하는 방식으로 하는게
// 다른 사람들이 보기에 더 명확하게 이게 무엇을 위해 사용하는 메서드인지 알 수 있을 것 같습니다
// 근데 이건 제 의견일 뿐이니 읽어보시고 괜찮은지 말해주시면 감사하겠습니다 !
// 다른 주석들도 마찬가지로 그저 제 의견일 뿐이니 읽어보시고 의견 공유하면 좋을 것 같습니다!
public class User extends Timestamped  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "유저 고유번호")
    private Long id;

//    단방향매핑으로 해도 될 것 같습니다.
//    해당 유저의 친구나 게시글을 조회 할 때에는
//    friend 테이블과 post 테이블에서 해당 user_id의 대한 정보만 select 해서 가져오면 될것 같습니다!!

//    @OneToMany(mappedBy = "from_user_id")
//    private List<Friend> friend = new ArrayList<>();
//    @OneToMany(mappedBy = "user")
//    private List<Post> post = new ArrayList<>();

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
}
