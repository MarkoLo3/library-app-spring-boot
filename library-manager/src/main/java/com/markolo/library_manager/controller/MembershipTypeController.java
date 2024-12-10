package com.markolo.library_manager.controller;

import com.markolo.library_manager.exception.AlreadyExistsException;
import com.markolo.library_manager.exception.ResourceNotFoundException;
import com.markolo.library_manager.model.MembershipType;
import com.markolo.library_manager.request.CreateMembershipTypeRequest;
import com.markolo.library_manager.response.ApiResponse;
import com.markolo.library_manager.service.membership.MembershipTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/membershipType/")
public class MembershipTypeController {

    private final MembershipTypeService membershipTypeService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> createMembershipType(@RequestBody CreateMembershipTypeRequest request)
    {
        try {
            MembershipType membershipType = membershipTypeService.createMembershipType(request);
            return ResponseEntity.ok(new ApiResponse("Success", membershipType));
        }
        catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/delete/{membershipTypeId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> deleteMembershipType(@PathVariable Long membershipTypeId) {
        try {
            membershipTypeService.deleteMembershipType(membershipTypeId);
            return ResponseEntity.ok(new ApiResponse("Success", null));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}

