package com.markolo.library_manager.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class CreateLoanRequest {
    private Long bookId;
    private Long userId;
    private String comments;
}
