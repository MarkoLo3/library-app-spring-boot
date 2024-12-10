package com.markolo.library_manager.service.membership;

import com.markolo.library_manager.exception.ResourceNotFoundException;
import com.markolo.library_manager.model.Membership;
import com.markolo.library_manager.model.MembershipStatus;
import com.markolo.library_manager.model.MembershipType;
import com.markolo.library_manager.model.User;
import com.markolo.library_manager.repository.MembershipRepository;
import com.markolo.library_manager.repository.MembershipTypeRepository;
import com.markolo.library_manager.repository.UserRepository;
import com.markolo.library_manager.request.CreateMembershipRequest;
import com.markolo.library_manager.request.UpdateMembershipRequest;
import com.markolo.library_manager.response.MembershipStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembershipService implements IMembershipService{

    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final MembershipTypeRepository membershipTypeRepository;

    @Override
    public Membership createMembership(CreateMembershipRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found!"));


        MembershipType membershipType = membershipTypeRepository.findById(request.getMembershipTypeId())
                .orElseThrow(()-> new ResourceNotFoundException("Membership type not found!"));

        Optional<Membership> activeMembership = membershipRepository.findByUserIdAndStatus(request.getUserId(), MembershipStatus.ACTIVE);

        if(activeMembership.isPresent()) {
            throw new IllegalStateException("User already has an active membership with ID: " + activeMembership.get().getId());
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(membershipType.getDurationInMonths());

        Membership membership = new Membership();

        membership.setUser(user);
        membership.setMembershipType(membershipType);
        membership.setStartDate(startDate);
        membership.setEndDate(endDate);
        membership.setPrice(membershipType.getPrice());
        membership.setStatus(MembershipStatus.ACTIVE);
        membership.setPayed(request.isPayed());

        return membershipRepository.save(membership);
    }

    @Override
    public Membership updateMembership(Long membershipId, UpdateMembershipRequest request) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(()-> new ResourceNotFoundException("Membership not found!"));

        if(request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(()-> new ResourceNotFoundException("User not found!"));
            membership.setUser(user);
        }
        if (request.getMembershipTypeId() != null) {
            MembershipType membershipType = membershipTypeRepository.findById(request.getMembershipTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("MembershipType not found with ID: " + request.getMembershipTypeId()));
            membership.setMembershipType(membershipType);
        }

        if (request.getStartDate() != null) {
            membership.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            membership.setEndDate(request.getEndDate());
        }

        if (request.getPrice() != null) {
            membership.setPrice(request.getPrice());
        }

        if (request.getPayed() != null) {
            membership.setPayed(request.getPayed());
        }

        if (request.getStatus() != null) {
            membership.setStatus(MembershipStatus.valueOf(request.getStatus().toUpperCase()));
        }
        return membershipRepository.save(membership);
    }

    @Override
    public void deleteMembership(Long membershipId) {
        membershipRepository.findById(membershipId).ifPresentOrElse(membershipRepository::delete, ()->
        {
            throw new ResourceNotFoundException("Membership not found!");
        });
    }

    @Override
    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    @Override
    public MembershipStatusResponse checkStatusAndPayment() { // no good

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username))
                .getId();

        List<Membership> memberships = membershipRepository.findByUserId(userId);

        if (memberships.isEmpty()) {
            return new MembershipStatusResponse("No memberships found",0.0, null);
        }

        double totalAmountDue = 0.0;
        Membership currentMembership = null;

        for (Membership membership : memberships) {
            if (!membership.isPayed()) {
                totalAmountDue += membership.getPrice();
            }

            if (membership.getStatus() == MembershipStatus.ACTIVE) {
                currentMembership = membership;
            }
        }

        String status = currentMembership != null ? currentMembership.getStatus().toString() : "No active membership";
        double currentPrice = currentMembership != null ? currentMembership.getPrice() : 0.0;

        return new MembershipStatusResponse(status, totalAmountDue, currentMembership != null ? currentMembership.getMembershipType().getType() : null);
    }
}
