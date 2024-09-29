package com.maxzamota.spring_sandbox.configuration.auth;

import com.maxzamota.spring_sandbox.model.UserEntity;
import com.maxzamota.spring_sandbox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthProvider implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthProvider(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        List<UserEntity> users = userService.getByEmail(username)
                .stream().filter(UserEntity::getIsActive).toList();
        if (!users.isEmpty()) {
            if (encoder.matches(password, users.getFirst().getPassword())) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(users.getFirst().getRole().toString()));
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            }
            throw new BadCredentialsException("Invalid password");
        }
        throw new BadCredentialsException("User not found");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
