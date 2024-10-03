package com.ust.securerestapi.auth.repo;

import com.ust.securerestapi.auth.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUserName(String userName);
}
