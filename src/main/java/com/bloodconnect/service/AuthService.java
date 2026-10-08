package com.bloodconnect.service;

import com.bloodconnect.dto.AuthResponse;
import com.bloodconnect.dto.LoginRequest;
import com.bloodconnect.dto.RegisterRequest;
import com.bloodconnect.model.User;
import com.bloodconnect.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email().trim())) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPhone(request.phone().trim());
        user.setBloodGroup(request.bloodGroup());
        user.setLocation(request.location().trim());
        user.setRole("USER");
        return toResponse(userRepository.save(user));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new IllegalArgumentException("Email or password is incorrect."));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Email or password is incorrect.");
        }
        return toResponse(user);
    }

    private AuthResponse toResponse(User user) {
        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone(),
                user.getBloodGroup(), user.getLocation(), user.getRole());
    }
}
