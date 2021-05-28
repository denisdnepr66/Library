package com.example.demo.some.DTO;

import com.example.demo.some.models.User;
import com.example.demo.some.security.Role;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserDTO {
    private final Long id;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final Role role;
    private final List<String> books = new ArrayList<>();
    private final List<String> requestedBooks = new ArrayList<>();

    public UserDTO(User user) {
        id = user.getId();
        email = user.getEmail();
        password = user.getPassword();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        role = user.getRole();
        user.getBooks().forEach(book -> books.add(book.getTitle()));
        user.getRequestedBooks().forEach(book -> requestedBooks.add(book.getTitle()));
    }
}
