package com.markolo.library_manager.service.membership;

import com.markolo.library_manager.exception.ResourceNotFoundException;
import com.markolo.library_manager.model.MembershipType;
import com.markolo.library_manager.repository.MembershipTypeRepository;
import com.markolo.library_manager.request.CreateMembershipTypeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipTypeService implements IMembershipTypeService {

    private final MembershipTypeRepository membershipTypeRepository;

    @Override
    public MembershipType createMembershipType(CreateMembershipTypeRequest request) {
        MembershipType membershipType = new MembershipType();
        membershipType.setType(request.getType());
        membershipType.setPrice(request.getPrice());
        membershipType.setDurationInMonths(request.getDurationInMonths());
        return membershipTypeRepository.save(membershipType);

    }

    @Override
    public void deleteMembershipType(Long membershipTypeId) {
        membershipTypeRepository.findById(membershipTypeId).ifPresentOrElse(membershipTypeRepository::delete, ()-> {
            throw new ResourceNotFoundException("Membership type not found!");
        });
    }
}
