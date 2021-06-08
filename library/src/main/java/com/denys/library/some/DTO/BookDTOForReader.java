package com.denys.library.some.DTO;

import com.denys.library.some.models.Book;
import lombok.Getter;

@Getter
public class BookDTOForReader extends BookDTO{
    public BookDTOForReader(Book book) {
        super(book);
    }
}
