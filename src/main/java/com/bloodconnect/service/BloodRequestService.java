package com.bloodconnect.service;

import com.bloodconnect.model.BloodRequest;
import com.bloodconnect.repository.BloodRequestRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BloodRequestService {
    private final BloodRequestRepository bloodRequestRepository;

    public BloodRequestService(BloodRequestRepository bloodRequestRepository) {
        this.bloodRequestRepository = bloodRequestRepository;
    }

    public BloodRequest create(BloodRequest request) {
        request.setPatientName(request.getPatientName().trim());
        request.setBloodGroup(request.getBloodGroup().trim().toUpperCase());
        request.setHospitalName(request.getHospitalName().trim());
        request.setLocation(request.getLocation().trim());
        request.setContactNumber(request.getContactNumber().trim());
        return bloodRequestRepository.save(request);
    }

    public List<BloodRequest> findAll() {
        return bloodRequestRepository.findAllByOrderByCreatedAtDesc();
    }
}
