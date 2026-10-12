package com.bloodconnect.controller;

import com.bloodconnect.model.BloodRequest;
import com.bloodconnect.dto.BloodRequestSummary;
import com.bloodconnect.service.BloodRequestService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/blood-requests")
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;

    public BloodRequestController(BloodRequestService bloodRequestService) {
        this.bloodRequestService = bloodRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BloodRequestSummary create(@Valid @RequestBody BloodRequest request) {
        return toSummary(bloodRequestService.create(request));
    }

    @GetMapping
    public List<BloodRequestSummary> findAll() {
        return bloodRequestService.findAll().stream().map(this::toSummary).toList();
    }

    private BloodRequestSummary toSummary(BloodRequest request) {
        return new BloodRequestSummary(request.getId(), request.getPatientName(), request.getBloodGroup(),
                request.getUnitsRequired(), request.getHospitalName(), request.getLocation(), request.getUrgency(),
                request.getMessage(), request.getRequiredDate(), request.getCreatedAt());
    }
}
