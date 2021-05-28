package com.example.demo.some.DTO;

import com.example.demo.some.models.Book;
import lombok.Getter;

@Getter
public class BookDTO {

    private final String title;
    private final String author;
    private final int yearPublished;
    private final String shortDescription;
    private final boolean available;
    private final int queueLength;

    public BookDTO(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.yearPublished = book.getYearPublished();
        this.shortDescription = book.getShortDescription();
        this.available = book.getUser() == null;
        this.queueLength = book.getUsersInQueue().size();
    }

}
