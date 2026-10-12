package com.bloodconnect.service;

import com.bloodconnect.model.Donor;
import com.bloodconnect.repository.DonorRepository;
import com.bloodconnect.dto.DonorSummary;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

@Service
public class DonorService {
    private final DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public Donor create(Donor donor) {
        donor.setName(donor.getName().trim());
        donor.setBloodGroup(donor.getBloodGroup().trim().toUpperCase());
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

    public Donor updateAvailability(Long donorId, Boolean available) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Donor not found."));
        donor.setAvailable(available);
        return donorRepository.save(donor);
    }

    public DonorSummary toSummary(Donor donor) {
        return new DonorSummary(donor.getId(), donor.getName(), donor.getBloodGroup(), donor.getLocation(),
                donor.getLastDonationDate(), donor.getAvailable());
    }

    public List<DonorSummary> toSummaries(List<Donor> donors) {
        return donors.stream().map(this::toSummary).toList();
    }
}
