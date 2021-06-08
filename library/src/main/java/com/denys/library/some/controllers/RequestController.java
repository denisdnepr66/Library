package com.denys.library.some.controllers;

import com.denys.library.some.models.Book;
import com.denys.library.some.models.User;
import com.denys.library.some.services.BookService;
import com.denys.library.some.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    BookService bookService;
    @Autowired
    UserService userService;

    @PutMapping("/title={title}")
    public ResponseEntity<?> makeRequest(@PathVariable String title){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails)principal).getUsername();
        } else {
            email = principal.toString();
        }

        User user = userService.findByEmail(email).orElseThrow(NoSuchElementException::new);

        Book book = bookService.findByTitle(title).orElseThrow(NoSuchElementException::new);

        if (!user.getBooks().contains(book) && !user.getRequestedBooks().contains(book)){
            user.addRequestedBook(book);
            book.addUserToQueue(user);
            userService.saveUser(user);
            bookService.saveBook(book);
            return new ResponseEntity<>(HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
