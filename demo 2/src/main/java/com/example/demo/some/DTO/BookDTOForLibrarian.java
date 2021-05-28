package com.example.demo.some.DTO;

import com.example.demo.some.models.Book;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookDTOForLibrarian extends BookDTO{

    private final List<ShortUserDTO> usersInQueue = new ArrayList<>();

    public BookDTOForLibrarian(Book book) {
        super(book);
        book.getUsersInQueue().forEach( userInQueue ->
                usersInQueue.add(new ShortUserDTO(userInQueue))
        );
    }
}
