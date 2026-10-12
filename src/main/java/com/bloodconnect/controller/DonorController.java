package com.bloodconnect.controller;

import com.bloodconnect.model.Donor;
import com.bloodconnect.dto.AvailabilityUpdate;
import com.bloodconnect.dto.DonorSummary;
import com.bloodconnect.service.DonorService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/donors")
public class DonorController {
    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DonorSummary create(@Valid @RequestBody Donor donor) {
        return donorService.toSummary(donorService.create(donor));
    }

    @GetMapping({ "", "/search" })
    public List<DonorSummary> search(
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String location) {
        return donorService.toSummaries(donorService.search(bloodGroup, location));
    }

    @PutMapping("/{id}/availability")
    public DonorSummary updateAvailability(@PathVariable Long id, @Valid @RequestBody AvailabilityUpdate update) {
        return donorService.toSummary(donorService.updateAvailability(id, update.available()));
    }
}
