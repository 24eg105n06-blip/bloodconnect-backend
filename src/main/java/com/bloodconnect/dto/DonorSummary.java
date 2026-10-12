package com.bloodconnect.dto;

import java.time.LocalDate;

public record DonorSummary(
        Long id,
        String name,
        String bloodGroup,
        String location,
        LocalDate lastDonationDate,
        Boolean available) {
}