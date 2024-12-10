package com.markolo.library_manager.repository;

import com.markolo.library_manager.model.Loan;
import com.markolo.library_manager.request.BorrowedBooksRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("SELECT new com.markolo.library_manager.request.BorrowedBooksRequest(b.title, b.author, l.expectedReturnDate, l.loanStatus) " +
            "FROM Loan l JOIN l.book b WHERE l.user.id = :userId")
    List<BorrowedBooksRequest> getLoansByUserId(Long userId);


    List<Loan> findByUserId(Long userId);

    void deleteByUserId(Long userId);

}
