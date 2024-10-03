package com.ust.securerestapi.auth.controller;

import com.ust.securerestapi.auth.domain.AppUser;
import com.ust.securerestapi.auth.domain.AppUserRole;
import com.ust.securerestapi.auth.jwt.JwtService;
import com.ust.securerestapi.auth.repo.UserRepository;
import com.ust.securerestapi.auth.service.ApiUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ApiUserService apiUserService;
    private final AuthenticationManager authenticationManager;
    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

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
    public ResponseEntity<Map<?,?>> login(@RequestBody LoginRequest request) {
        log.debug("Login request: {}", request);

        Map<Object,Object> response = new HashMap<>();
        response.put("token", "");
        response.put("authenticated", false);

        Authentication authRequest = UsernamePasswordAuthenticationToken
                                        .unauthenticated(request.userName(), request.password());
        Authentication authResult = authenticationManager.authenticate(authRequest);
        log.debug("Auth result: {}", authResult);
        if (authResult.isAuthenticated()) {
            UserDetails user = apiUserService.loadUserByUsername(request.userName());
            log.info("User: {}", request.userName());
            log.info("User: {}", user.getUsername());
            response.put("token", jwtService.generateToken(user));
            response.put("authenticated", true);
        }
        response.put("status", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Error: ", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
