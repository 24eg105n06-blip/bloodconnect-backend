package com.bloodconnect.dto;

import jakarta.validation.constraints.NotNull;

public record AvailabilityUpdate(@NotNull Boolean available) {
}