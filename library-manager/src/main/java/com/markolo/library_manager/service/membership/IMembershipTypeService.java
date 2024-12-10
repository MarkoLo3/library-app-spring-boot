package com.markolo.library_manager.service.membership;

import com.markolo.library_manager.model.MembershipType;
import com.markolo.library_manager.request.CreateMembershipTypeRequest;

public interface IMembershipTypeService {
    MembershipType createMembershipType(CreateMembershipTypeRequest request);
    void deleteMembershipType(Long membershipTypeId);
}
