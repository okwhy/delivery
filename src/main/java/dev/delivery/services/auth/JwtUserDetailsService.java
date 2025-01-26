package dev.delivery.services.auth;

import dev.delivery.dtos.auth.AuthUser;
import dev.delivery.dtos.projections.PerformerProjection;
import dev.delivery.entities.PerformerEntity;
import dev.delivery.repos.PerformerRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final PerformerRepo performerRepo;

    @Override
    public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
        PerformerEntity user = performerRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
        return AuthUser.builder()
                .id(user.getId())
                .username(user.getCredential().getUsername())
                .password(user.getCredential().getPassword().getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
                .enabled(true)
                .build();
    }

    public AuthUser loadUserById(Long id) throws UsernameNotFoundException {
        PerformerProjection user = performerRepo.findProjectionById(id)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with id %s not found", id)));
        return AuthUser.builder()
                .id(user.getId())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
                .build();
    }

}
