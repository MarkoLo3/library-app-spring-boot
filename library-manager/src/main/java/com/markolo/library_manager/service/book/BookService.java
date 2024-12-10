package com.markolo.library_manager.service.book;

import com.markolo.library_manager.exception.ResourceNotFoundException;
import com.markolo.library_manager.model.Book;
import com.markolo.library_manager.repository.BookRepository;
import com.markolo.library_manager.request.AvailableBooksRequest;
import com.markolo.library_manager.request.CreateBookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BookService implements IBookService {

    private final BookRepository bookRepository;


    @Override
    public Book createBook(CreateBookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setQuantity(request.getQuantity());
        book.setPublishedYear(request.getPublishedYear());
        book.setGenre(request.getGenre());

        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(CreateBookRequest request, Long bookId) {
        return bookRepository.findById(bookId).map(existingBook ->{
            existingBook.setTitle(request.getTitle());
            existingBook.setAuthor(request.getAuthor());
            existingBook.setIsbn(request.getIsbn());
            existingBook.setQuantity(request.getQuantity());
            existingBook.setPublishedYear(request.getPublishedYear());
            existingBook.setGenre(request.getGenre());
            return bookRepository.save(existingBook);
        }).orElseThrow(()-> new ResourceNotFoundException("Book not found!"));
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.findById(bookId).ifPresentOrElse(bookRepository::delete,()->{
            throw new ResourceNotFoundException("Book not found!");
        });
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<AvailableBooksRequest> getAllAvailableBooks() {
        return bookRepository.getAllAvailableBooks();
    }


}
