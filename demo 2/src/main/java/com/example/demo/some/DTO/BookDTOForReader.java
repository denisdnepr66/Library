package com.example.demo.some.DTO;

import com.example.demo.some.models.Book;
import lombok.Getter;

@Getter
public class BookDTOForReader extends BookDTO{
    public BookDTOForReader(Book book) {
        super(book);
    }
}
