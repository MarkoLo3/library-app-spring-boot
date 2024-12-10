package com.markolo.library_manager.request;

import com.markolo.library_manager.model.LoanStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class PatchLoanRequest {
    private LoanStatus loanStatus;
    private LocalDate actualReturnDate;
}
