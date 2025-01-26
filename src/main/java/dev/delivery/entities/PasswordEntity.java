package dev.delivery.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "password")
public class PasswordEntity {
    static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public PasswordEntity(String password) {
        this.password = encoder.encode(password);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String password;

    @JsonIgnore
    private void setPassword(String password) {
        this.password = encoder.encode(password);
    }
}