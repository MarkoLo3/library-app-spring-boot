package com.markolo.library_manager.request;


import com.markolo.library_manager.model.Genre;
import lombok.Data;

@Data
public class CreateBookRequest {

    private String title;
    private String author;
    private String isbn;
    private int quantity;
    private int publishedYear;
    private Genre genre;

}
