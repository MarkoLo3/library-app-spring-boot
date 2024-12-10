package com.markolo.library_manager.controller;

import com.markolo.library_manager.exception.AlreadyExistsException;
import com.markolo.library_manager.exception.ResourceNotFoundException;
import com.markolo.library_manager.model.Membership;
import com.markolo.library_manager.request.CreateMembershipRequest;
import com.markolo.library_manager.request.UpdateMembershipRequest;
import com.markolo.library_manager.response.ApiResponse;
import com.markolo.library_manager.response.MembershipStatusResponse;
import com.markolo.library_manager.service.membership.IMembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/memberships")
public class MembershipController {
    private final IMembershipService membershipService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> createMembership(@RequestBody CreateMembershipRequest request) {
        try {
            Membership membership = membershipService.createMembership(request);
            return ResponseEntity.ok(new ApiResponse("Create Membership Success!", membership));
        }
        catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/update/{membershipId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> updateMembership(@PathVariable Long membershipId, @RequestBody UpdateMembershipRequest request) {
        try {
            Membership membership = membershipService.updateMembership(membershipId, request);
            return ResponseEntity.ok(new ApiResponse("Membership updated successfully", membership));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @DeleteMapping("/delete/{membershipId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> deleteMembership(@PathVariable Long membershipId) {
        try {
            membershipService.deleteMembership(membershipId);
            return ResponseEntity.ok(new ApiResponse("Deleting Membership success!", null));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse> getMembershipStatus() {
        try
        {
            MembershipStatusResponse response = membershipService.checkStatusAndPayment();
            return ResponseEntity.ok(new ApiResponse("success!", response));

        }
        catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }

    @GetMapping("/getAllMemberships")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> getAllMemberships() {
        try
        {
            List<Membership> membershipList = membershipService.getAllMemberships();
            return ResponseEntity.ok(new ApiResponse("Success", membershipList));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


}
