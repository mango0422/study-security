package mango.security.authorizationServer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    // 역할(Role) 정보를 단순 문자열로 저장 (쉼표로 구분 등)
    @Column(nullable = false, length = 500)
    private String roles;
}
