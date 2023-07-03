package umc.reco.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @JsonIgnore
    private Long id;

    private Long uuid;

    private String email;

    @OneToOne(mappedBy = "member")
    private Tree tree;

    private String nickname;

    public Member(Long uuid, String email) {
        this.uuid = uuid;
        this.email = email;
    }
}
