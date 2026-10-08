package com.bloodconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotBlank @Pattern(regexp = "^[+0-9() -]{7,20}$") String phone,
        @NotBlank String bloodGroup,
        @NotBlank String location) {
}
