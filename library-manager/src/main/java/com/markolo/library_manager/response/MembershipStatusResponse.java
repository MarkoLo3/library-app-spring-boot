package com.markolo.library_manager.response;

import com.markolo.library_manager.model.MembershipType;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipStatusResponse {

    private String status;
    private double totalAmountDue;
    private String Type;
}
