package com.bloodconnect.repository;

import com.bloodconnect.model.Donor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorRepository extends JpaRepository<Donor, Long> {
    Optional<Donor> findFirstByPhoneOrderByIdAsc(String phone);

    List<Donor> findByAvailableTrueOrderByNameAsc();

    List<Donor> findByAvailableTrueAndBloodGroupIgnoreCaseOrderByNameAsc(String bloodGroup);

    List<Donor> findByAvailableTrueAndLocationContainingIgnoreCaseOrderByNameAsc(String location);

    List<Donor> findByAvailableTrueAndBloodGroupIgnoreCaseAndLocationContainingIgnoreCaseOrderByNameAsc(
            String bloodGroup, String location);
}
