package com.markolo.library_manager.service.loan;

import com.markolo.library_manager.model.Loan;
import com.markolo.library_manager.request.BorrowedBooksRequest;
import com.markolo.library_manager.request.CreateLoanRequest;
import com.markolo.library_manager.request.PatchLoanRequest;
import com.markolo.library_manager.request.UpdateLoanRequest;

import java.util.List;

public interface ILoanService {
    List<Loan> getAllLoans();
    Loan createLoan(CreateLoanRequest request);
    Loan updateLoan(Long loanId,UpdateLoanRequest request); // TO DO
    void deleteLoan(Long loanId);
    Loan updateLoanStatusAndReturnDate(Long loanId, PatchLoanRequest request); // not needed
    List<BorrowedBooksRequest> getLoansByUserId(Long userId);
    Loan returnBook(Long loanId);
    List<BorrowedBooksRequest> getBorrowedBooks();

}
