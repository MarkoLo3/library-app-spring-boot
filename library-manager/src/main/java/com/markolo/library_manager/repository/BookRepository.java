package com.markolo.library_manager.repository;

import com.markolo.library_manager.model.Book;
import com.markolo.library_manager.request.AvailableBooksRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT new com.markolo.library_manager.request.AvailableBooksRequest(" +
            "b.title, b.author, b.publishedYear, b.genre) " +
            "FROM Book b WHERE b.quantity > 0")
    List<AvailableBooksRequest> getAllAvailableBooks();

}
