package com.markolo.library_manager.service.book;

import com.markolo.library_manager.model.Book;
import com.markolo.library_manager.request.AvailableBooksRequest;
import com.markolo.library_manager.request.CreateBookRequest;

import java.util.List;

public interface IBookService {
    Book createBook(CreateBookRequest request);
    Book updateBook(CreateBookRequest request, Long bookId);
    void deleteBook(Long bookId);
    List<Book> getAllBooks();
    List<AvailableBooksRequest>  getAllAvailableBooks();
}
