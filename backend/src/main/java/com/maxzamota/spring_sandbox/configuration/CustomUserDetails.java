package com.maxzamota.spring_sandbox.configuration;

import com.maxzamota.spring_sandbox.model.UserEntity;
import com.maxzamota.spring_sandbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetails implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName;
        String password;
        List<GrantedAuthority> authorityList = null;
        Collection<UserEntity> users = userRepository.findByEmail(username);
        if (users.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        userName = users.stream().findFirst().get().getEmail();
        password = users.stream().findFirst().get().getPassword();
        authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(users.stream().findFirst().get().getRole()));
        return new User(username, password, authorityList);
    }
}
