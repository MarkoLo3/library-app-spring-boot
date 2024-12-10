package com.markolo.library_manager.controller;


import com.markolo.library_manager.exception.ResourceNotFoundException;
import com.markolo.library_manager.model.Loan;
import com.markolo.library_manager.request.BorrowedBooksRequest;
import com.markolo.library_manager.request.CreateLoanRequest;
import com.markolo.library_manager.request.PatchLoanRequest;
import com.markolo.library_manager.request.UpdateLoanRequest;
import com.markolo.library_manager.response.ApiResponse;
import com.markolo.library_manager.service.loan.ILoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loans")
public class LoanController {

    private final ILoanService loanService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> createLoan(@RequestBody CreateLoanRequest request) {
        try {
            Loan loan = loanService.createLoan(request);
            return ResponseEntity.ok(new ApiResponse("Create Loan Success!", loan));
        }
        catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/getAllLoans")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> getAllLoans() {
        try {
            List<Loan> loans = loanService.getAllLoans();
            return ResponseEntity.ok(new ApiResponse("Success", loans));
        }
        catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{loanId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> deleteLoan(@PathVariable Long loanId) {
        try {
            loanService.deleteLoan(loanId);
            return ResponseEntity.ok(new ApiResponse("Deleting loan success!", null));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PatchMapping("/updateLoanStatus/{loanId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> updateLoanStatusAndReturnDate(@RequestBody PatchLoanRequest request, @PathVariable Long loanId) {
        try {
            Loan loan = loanService.updateLoanStatusAndReturnDate(loanId, request);
            return ResponseEntity.ok(new ApiResponse("Loan updated successfully", loan));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> getLoansByUserId(@PathVariable Long userId) {
        // gets all the borrowed books for a specific user
        //
        try
        {
            List<BorrowedBooksRequest> loans = loanService.getLoansByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Success", loans));
        }
        catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PatchMapping("/returnBook/{loanId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> returnBook(@PathVariable Long loanId) {
        try {
            Loan loan = loanService.returnBook(loanId);
            return ResponseEntity.ok(new ApiResponse("Success", loan));
        }
        catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    // method for the authenticated user
    @GetMapping("/my-borrowed-books")
    public ResponseEntity<ApiResponse> getMyBorrowedBooks() {
        try
        {
            List<BorrowedBooksRequest> borrowedBooks = loanService.getBorrowedBooks();
            return ResponseEntity.ok(new ApiResponse("Success", borrowedBooks));

        }
        catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @PutMapping("/update/{loanId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> updateLoan(@PathVariable Long loanId, @RequestBody UpdateLoanRequest request) {
        try
        {
            Loan updatedLoan = loanService.updateLoan(loanId, request);
            return ResponseEntity.ok(new ApiResponse("Success", updatedLoan));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }
}
