package com.ust.securerestapi.auth.service;

import com.ust.securerestapi.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Fetching the user details from the database
        var dbUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Mapping the user roles to a Set of GrantedAuthority
//        Set<GrantedAuthority> authorities = dbUser.getRoles().stream()
//                .map(role -> (GrantedAuthority) role::getName)
//                .collect(Collectors.toSet());

        return User
                .builder()
                .username(dbUser.getUsername())
                .password(dbUser.getPassword())
                .authorities(dbUser.getRoles().stream().map(role -> role.getName()).toArray(String[]::new))
                .build();
    }
}
