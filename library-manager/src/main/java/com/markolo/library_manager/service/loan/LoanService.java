package com.markolo.library_manager.service.loan;

import com.markolo.library_manager.exception.ResourceNotFoundException;
import com.markolo.library_manager.model.*;
import com.markolo.library_manager.repository.BookRepository;
import com.markolo.library_manager.repository.LoanRepository;
import com.markolo.library_manager.repository.MembershipRepository;
import com.markolo.library_manager.repository.UserRepository;
import com.markolo.library_manager.request.BorrowedBooksRequest;
import com.markolo.library_manager.request.CreateLoanRequest;
import com.markolo.library_manager.request.PatchLoanRequest;
import com.markolo.library_manager.request.UpdateLoanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService implements ILoanService{

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final MembershipRepository membershipRepository;

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
    @Override
    public List<BorrowedBooksRequest> getLoansByUserId(Long userId) { // no good
        return loanRepository.getLoansByUserId(userId);
    }
    //method for the authenticated user
    public List<BorrowedBooksRequest> getBorrowedBooks() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username))
                .getId();
        List<Loan> loans = loanRepository.findByUserId(userId);


        return loans.stream()
                .map(loan -> new BorrowedBooksRequest(
                        loan.getBook().getTitle(),
                        loan.getBook().getAuthor(),
                        loan.getExpectedReturnDate(),
                        loan.getLoanStatus()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public Loan createLoan(CreateLoanRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found!"));

        boolean hasActiveMembership = membershipRepository.findByUserIdAndStatus(user.getId(), MembershipStatus.ACTIVE).isPresent();
        if (!hasActiveMembership) {
            throw new IllegalStateException("User does not have an active membership and cannot loan books.");
        }

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(()-> new ResourceNotFoundException("Book not found!"));



        if(book.getQuantity() < 1) {
            throw new IllegalStateException("Book is not available for loan");
        }
        book.setQuantity(book.getQuantity()-1);
        bookRepository.save(book);
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setExpectedReturnDate(LocalDate.now().plusDays(14)); // default loan 14 days
        loan.setLoanStatus(LoanStatus.BORROWED);
        loan.setComments(request.getComments());
        return loanRepository.save(loan);

    }

    @Override
    public Loan updateLoan(Long loanId,UpdateLoanRequest request) {
        Loan existingLoan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found with ID: " + loanId));


        if (request.getLoanDate() != null) {
            existingLoan.setLoanDate(request.getLoanDate());
        }
        if (request.getExpectedReturnDate() != null) {
            existingLoan.setExpectedReturnDate(request.getExpectedReturnDate());
        }
        if (request.getActualReturnDate() != null) {
            existingLoan.setActualReturnDate(request.getActualReturnDate());
        }
        if (request.getLoanStatus() != null) {
            existingLoan.setLoanStatus(request.getLoanStatus());
        }
        if (request.getComments() != null) {
            existingLoan.setComments(request.getComments());
        }


        return loanRepository.save(existingLoan);
    }

    @Override
    public void deleteLoan(Long loanId) {

        loanRepository.findById(loanId).ifPresentOrElse(loanRepository::delete,
                () -> {throw new ResourceNotFoundException("Loan not found!");});

    }


    @Override //method not needed
    public Loan updateLoanStatusAndReturnDate(Long loanId, PatchLoanRequest request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if(request.getLoanStatus() == LoanStatus.RETURNED && request.getActualReturnDate()==null) {
            throw new IllegalArgumentException("Actual return date must be provided when marking the loan as RETURNED");
        }
        loan.setLoanStatus(request.getLoanStatus());
        if(request.getActualReturnDate() != null) {
            loan.setActualReturnDate(request.getActualReturnDate());
        }
        return loanRepository.save(loan);

    }

    @Override
    public Loan returnBook(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found with ID: " + loanId));


        if (loan.getLoanStatus() == LoanStatus.RETURNED) {
            throw new IllegalStateException("This loan has already been returned.");
        }


        Book book = loan.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);


        loan.setLoanStatus(LoanStatus.RETURNED);
        loan.setActualReturnDate(LocalDate.now());


        return loanRepository.save(loan);
    }
}
