package com.example.shop.service;

import com.example.shop.controller.dto.RegisterRequest;
import com.example.shop.model.UserAccount;
import com.example.shop.repository.UserAccountRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest req) {
        if (userRepo.findByUsername(req.username()).isPresent()) {
            throw new EntityExistsException("User already exists");
        }

        validatePassword(req.password());

        UserAccount ua = new UserAccount();
        ua.setUsername(req.username());
        ua.setPassword(passwordEncoder.encode(req.password())); // 🔥 ВАЖНО: шифруем
        ua.setRole("ROLE_USER");

        userRepo.save(ua);
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password too short");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain a digit");
        }
        if (!password.matches(".*[!@#$%^&*().,;:+-].*")) {
            throw new IllegalArgumentException("Password must contain a special symbol");
        }
    }
}
