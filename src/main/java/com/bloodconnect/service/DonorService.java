package com.bloodconnect.service;

import com.bloodconnect.model.Donor;
import com.bloodconnect.repository.DonorRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DonorService {
    private final DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public Donor create(Donor donor) {
        donor.setName(donor.getName().trim());
        donor.setPhone(donor.getPhone().trim());
        donor.setLocation(donor.getLocation().trim());
        if (donor.getAvailable() == null)
            donor.setAvailable(true);
        return donorRepository.save(donor);
    }

    public List<Donor> search(String bloodGroup, String location) {
        boolean hasGroup = bloodGroup != null && !bloodGroup.isBlank() && !bloodGroup.equalsIgnoreCase("all");
        boolean hasLocation = location != null && !location.isBlank();
        String normalizedLocation = hasLocation ? location.trim() : "";

        if (hasGroup && hasLocation) {
            return donorRepository
                    .findByAvailableTrueAndBloodGroupIgnoreCaseAndLocationContainingIgnoreCaseOrderByNameAsc(
                            bloodGroup.trim(), normalizedLocation);
        }
        if (hasGroup) {
            return donorRepository.findByAvailableTrueAndBloodGroupIgnoreCaseOrderByNameAsc(bloodGroup.trim());
        }
        if (hasLocation) {
            return donorRepository.findByAvailableTrueAndLocationContainingIgnoreCaseOrderByNameAsc(normalizedLocation);
        }
        return donorRepository.findByAvailableTrueOrderByNameAsc();
    }
}
