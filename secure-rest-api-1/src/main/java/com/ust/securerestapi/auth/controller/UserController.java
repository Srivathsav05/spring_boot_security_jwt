package com.ust.securerestapi.auth.controller;

import com.ust.securerestapi.auth.domain.AppUser;
import com.ust.securerestapi.auth.domain.AppUserRole;
import com.ust.securerestapi.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // POST /api/users/register
    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody RegisterRequest request) {
        var user = new AppUser();
        user.setUserName(request.userName());
        user.setPassword(passwordEncoder.encode(request.password()));
        Set<AppUserRole> roles = Arrays.stream(request.roles())
                                .map(s -> new AppUserRole("ROLE_"+s))
                                .collect(Collectors.toSet());
        log.debug("Roles: {}", roles);
        user.setRoles(roles);
        var response = userRepository.save(user);
        return ResponseEntity.ok(response);
    }

    // POST /api/users/login
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request) {
        log.debug("Login request: {}", request);
    }
}
