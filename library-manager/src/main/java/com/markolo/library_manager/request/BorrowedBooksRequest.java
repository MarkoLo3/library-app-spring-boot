package com.markolo.library_manager.request;

import com.markolo.library_manager.model.LoanStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedBooksRequest {
    private String title;
    private String author;
    private LocalDate expectedReturnDate;
    private LoanStatus loanStatus;
}
