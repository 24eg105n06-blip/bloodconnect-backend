package com.bloodconnect.dto;

public record AuthResponse(
        Long id,
        String name,
        String email,
        String phone,
        String bloodGroup,
        String location,
        String role) {
}
