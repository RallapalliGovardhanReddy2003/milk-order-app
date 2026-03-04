package com.example.milk_order_app.service;

import com.example.milk_order_app.entity.AppUser;
import com.example.milk_order_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AppUser user = repository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));

        // 🔥 Force ROLE_ prefix manually
        String role = "ROLE_" + user.getRole().trim().toUpperCase();

        System.out.println("✅ Username: " + user.getUsername());
        System.out.println("✅ Role from DB: " + user.getRole());
        System.out.println("✅ Final Authority: " + role);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}