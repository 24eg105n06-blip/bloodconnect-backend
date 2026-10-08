package com.bloodconnect.repository;

import com.bloodconnect.model.BloodRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    List<BloodRequest> findAllByOrderByCreatedAtDesc();
}
