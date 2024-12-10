package com.markolo.library_manager.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateMembershipRequest {
    private Long userId;
    private Long membershipTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
    private Boolean payed;
    private String status;
}
