package dev.delivery.dtos.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


@Builder
@Getter
@Setter
public class AuthUser implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private boolean enabled;

    private List<SimpleGrantedAuthority> authorities;
}

