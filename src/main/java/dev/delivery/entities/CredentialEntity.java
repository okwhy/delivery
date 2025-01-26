package dev.delivery.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "credentials")
public class CredentialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String username;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private PasswordEntity password;

}