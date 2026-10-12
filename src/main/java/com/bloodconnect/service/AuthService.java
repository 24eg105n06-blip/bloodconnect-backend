
package com.bloodconnect.service;

import com.bloodconnect.dto.AuthResponse;
import com.bloodconnect.dto.LoginRequest;
import com.bloodconnect.dto.RegisterRequest;
import com.bloodconnect.dto.UpdateProfileRequest;
import com.bloodconnect.model.User;
import com.bloodconnect.model.Donor;
import com.bloodconnect.repository.DonorRepository;
import com.bloodconnect.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final DonorRepository donorRepository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository,
                       DonorRepository donorRepository) {
        this.userRepository = userRepository;
        this.donorRepository = donorRepository;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(
                request.email().trim())) {
            throw new IllegalArgumentException(
                    "An account with this email already exists.");
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPhone(request.phone().trim());
        user.setBloodGroup(request.bloodGroup().trim().toUpperCase());
        user.setLocation(request.location().trim());

        String role = request.role();
        user.setRole(role == null || role.isBlank()
                ? "DONOR" : role.trim().toUpperCase());

        User savedUser = userRepository.save(user);

        if ("DONOR".equals(savedUser.getRole())) {
            Donor donor = new Donor();
            donor.setName(savedUser.getName());
            donor.setBloodGroup(savedUser.getBloodGroup());
            donor.setPhone(savedUser.getPhone());
            donor.setLocation(savedUser.getLocation());
            donor.setAvailable(true);
            donorRepository.save(donor);
        }

        return toResponse(savedUser);
    }

    @Transactional
    public AuthResponse updateProfile(
            Long id, UpdateProfileRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        String email = request.email().trim().toLowerCase();

        userRepository.findByEmailIgnoreCase(email)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "That email is already used by another account");
                });

        // Find the donor using the OLD phone number first.
        Donor donor = "DONOR".equals(user.getRole())
                ? donorRepository.findFirstByPhoneOrderByIdAsc(
                        user.getPhone()).orElse(null)
                : null;

        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPhone(request.phone().trim());
        user.setBloodGroup(request.bloodGroup().trim().toUpperCase());
        user.setLocation(request.location().trim());

        User savedUser = userRepository.save(user);

        // Update the existing donor profile.
        if (donor != null) {
            donor.setName(savedUser.getName());
            donor.setBloodGroup(savedUser.getBloodGroup());
            donor.setPhone(savedUser.getPhone());
            donor.setLocation(savedUser.getLocation());
            donorRepository.save(donor);
        }

        return toResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(
                request.email().trim())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Email or password is incorrect."));

        if (!passwordEncoder.matches(
                request.password(), user.getPassword())) {
            throw new IllegalArgumentException(
                    "Email or password is incorrect.");
        }

        return toResponse(user);
    }

    private AuthResponse toResponse(User user) {
        Long donorId = "DONOR".equals(user.getRole())
                ? donorRepository.findFirstByPhoneOrderByIdAsc(
                        user.getPhone())
                        .map(Donor::getId).orElse(null)
                : null;

        return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getBloodGroup(),
                user.getLocation(),
                user.getRole(),
                donorId
        );
    }
}
