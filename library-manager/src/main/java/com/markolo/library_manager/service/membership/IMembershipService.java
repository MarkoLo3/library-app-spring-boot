package com.markolo.library_manager.service.membership;

import com.markolo.library_manager.model.Membership;
import com.markolo.library_manager.request.CreateMembershipRequest;
import com.markolo.library_manager.request.UpdateMembershipRequest;
import com.markolo.library_manager.response.MembershipStatusResponse;

import java.util.List;

public interface IMembershipService {
    Membership createMembership(CreateMembershipRequest request);
    Membership updateMembership(Long membershipId, UpdateMembershipRequest request);
    void deleteMembership(Long membershipId);

    List<Membership> getAllMemberships();


    MembershipStatusResponse checkStatusAndPayment(); // no good


}
