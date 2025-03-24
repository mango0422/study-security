package mango.security.token_login.entity;

import mango.security.token_login.type.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {
    @Id
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
