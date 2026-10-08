package com.bloodconnect.repository;

import com.bloodconnect.model.Donor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorRepository extends JpaRepository<Donor, Long> {
    List<Donor> findByAvailableTrueOrderByNameAsc();

    List<Donor> findByAvailableTrueAndBloodGroupIgnoreCaseOrderByNameAsc(String bloodGroup);

    List<Donor> findByAvailableTrueAndLocationContainingIgnoreCaseOrderByNameAsc(String location);

    List<Donor> findByAvailableTrueAndBloodGroupIgnoreCaseAndLocationContainingIgnoreCaseOrderByNameAsc(
            String bloodGroup, String location);
}
