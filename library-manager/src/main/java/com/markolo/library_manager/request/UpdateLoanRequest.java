package com.markolo.library_manager.request;

import com.markolo.library_manager.model.LoanStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateLoanRequest {
    private LocalDate loanDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private LoanStatus loanStatus;
    private String comments;
}
