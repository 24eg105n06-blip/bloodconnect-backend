package com.bloodconnect.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BloodRequestSummary(
        Long id,
        String patientName,
        String bloodGroup,
        Integer unitsRequired,
        String hospitalName,
        String location,
        String urgency,
        String message,
        LocalDate requiredDate,
        LocalDateTime createdAt) {
}