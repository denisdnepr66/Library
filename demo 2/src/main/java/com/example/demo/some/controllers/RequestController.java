package com.example.demo.some.controllers;

import com.example.demo.some.models.Book;
import com.example.demo.some.models.User;
import com.example.demo.some.services.BookService;
import com.example.demo.some.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void makeRequest(@PathVariable String title){
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
            System.out.println(book.getUsersInQueue());
        }
    }
}
