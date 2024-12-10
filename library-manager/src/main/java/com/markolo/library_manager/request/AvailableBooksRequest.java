package com.markolo.library_manager.request;

import com.markolo.library_manager.model.Genre;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AvailableBooksRequest {

    private String title;
    private String author;
    private int publishedYear;
    private Genre genre;

}
