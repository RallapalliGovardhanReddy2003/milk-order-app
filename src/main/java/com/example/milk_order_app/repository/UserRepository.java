package com.example.milk_order_app.repository;

import com.example.milk_order_app.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByMobile(String mobile);

    // 🔥 Multi-login support
    Optional<AppUser> findByUsernameOrEmailOrMobile(
            String username,
            String email,
            String mobile
    );
}