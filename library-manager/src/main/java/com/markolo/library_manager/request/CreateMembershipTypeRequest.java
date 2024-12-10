package com.markolo.library_manager.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CreateMembershipTypeRequest {

    private String type;
    private double price;
    private int durationInMonths;
}
