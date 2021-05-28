package com.example.demo.some.DTO;

import com.example.demo.some.models.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetUserBooksDTO extends ShortUserDTO{
    private final List<String> books = new ArrayList<>();
    private final List<String> requestedBooks = new ArrayList<>();
    public GetUserBooksDTO(User user) {
        super(user);
        user.getBooks().forEach(book -> books.add(book.getTitle()));
        user.getRequestedBooks().forEach(book -> requestedBooks.add(book.getTitle()));
    }
}
