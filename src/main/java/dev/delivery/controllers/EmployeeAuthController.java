package dev.delivery.controllers;

import dev.delivery.dtos.CredentialDto;
import dev.delivery.dtos.auth.AuthUser;
import dev.delivery.dtos.auth.JwtResponce;
import dev.delivery.services.auth.JwtTokenManager;
import dev.delivery.services.auth.JwtUserDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
@AllArgsConstructor
@Tag(name = "A. Auth API", description = "API для аутентификации сотрудников")
public class EmployeeAuthController {
    private final JwtUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager tokenManager;

    @PostMapping("/login")
    public JwtResponce createToken(@RequestBody CredentialDto credentials) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
        final AuthUser authUser = userDetailsService.loadUserByUsername(credentials.getUsername());
        return new JwtResponce(tokenManager.generateJwtToken(authUser));
    }
}
