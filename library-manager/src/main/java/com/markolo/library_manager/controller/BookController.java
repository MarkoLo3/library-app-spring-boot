package com.markolo.library_manager.controller;


import com.markolo.library_manager.exception.AlreadyExistsException;
import com.markolo.library_manager.exception.ResourceNotFoundException;
import com.markolo.library_manager.model.Book;
import com.markolo.library_manager.request.AvailableBooksRequest;
import com.markolo.library_manager.request.CreateBookRequest;
import com.markolo.library_manager.response.ApiResponse;
import com.markolo.library_manager.service.book.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

        private final IBookService bookService;


        @PostMapping("/add")
        @PreAuthorize("hasRole('EMPLOYEE')")
        public ResponseEntity<ApiResponse> createBook(@RequestBody CreateBookRequest request) {
            try {
                Book book = bookService.createBook(request);
                return ResponseEntity.ok(new ApiResponse("Create Book Success!", book));
            }
            catch (AlreadyExistsException e) {
                return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
            }
        }


        @GetMapping("/getAllBooks")
        @PreAuthorize("hasRole('EMPLOYEE')")
        public ResponseEntity<ApiResponse> getAllBooks() {
            try {
                List<Book> books = bookService.getAllBooks();
                return ResponseEntity.ok(new ApiResponse("Success", books));
            }
            catch (Exception e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }
        }


        @PutMapping("/update/{bookId}")
        @PreAuthorize("hasRole('EMPLOYEE')")
        public ResponseEntity<ApiResponse> updateBook(@RequestBody CreateBookRequest request, @PathVariable Long bookId) {
            try {
                Book book = bookService.updateBook(request, bookId);
                return ResponseEntity.ok(new ApiResponse("Book updated successfully", book));
            }
            catch (ResourceNotFoundException e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
            }
        }

        @DeleteMapping("/delete/{bookId}")
        @PreAuthorize("hasRole('EMPLOYEE')")
        public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long bookId) {
            try {
                bookService.deleteBook(bookId);
                return ResponseEntity.ok(new ApiResponse("Deleting book success!", null));
            }
            catch (ResourceNotFoundException e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }
        }

        @GetMapping("/getAvailableBooks") // member can view the available books
        public ResponseEntity<ApiResponse> getAvailableBooks() {
            try
            {
                List<AvailableBooksRequest> books =bookService.getAllAvailableBooks();
                return ResponseEntity.ok(new ApiResponse("Success", books));
            }
            catch (Exception e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }
        }




}
