package dev.delivery.entities;

import dev.delivery.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "performers")
public class PerformerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fio;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "credential")
    private CredentialEntity credential;

}
