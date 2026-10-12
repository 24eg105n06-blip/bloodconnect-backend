package com.bloodconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
                @NotBlank String name,
                @Email @NotBlank String email,
                @NotBlank String password,
                @NotBlank @Pattern(regexp = "^[+0-9() -]{7,20}$") String phone,
                @NotBlank @Pattern(regexp = "^(?i:A|B|AB|O)[+-]$") String bloodGroup,
                @NotBlank String location,
                @Pattern(regexp = "(?i)DONOR|REQUESTER|HOSPITAL") String role) {
}
