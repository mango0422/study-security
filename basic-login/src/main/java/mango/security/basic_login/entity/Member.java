package mango.security.basic_login.entity;

import io.lettuce.core.models.role.RedisInstance.Role;
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
