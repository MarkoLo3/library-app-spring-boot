package com.markolo.library_manager.request;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Setter
@Getter
@NoArgsConstructor
public class CreateMembershipRequest {
    private Long userId;
    private Long membershipTypeId;
    private boolean payed;

}
