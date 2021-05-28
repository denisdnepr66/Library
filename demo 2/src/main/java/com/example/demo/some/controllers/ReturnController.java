package com.example.demo.some.controllers;

import com.example.demo.some.models.Book;
import com.example.demo.some.models.User;
import com.example.demo.some.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/return")
public class ReturnController {

    @Autowired
    UserService userService;

    @PostMapping("/title={title}")
    public void returnBook(@PathVariable String title){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails)principal).getUsername();
        } else {
            email = principal.toString();
        }

        User user = userService.findByEmail(email).orElseThrow(NoSuchElementException::new);

        Book book = user.getBooks()
                .stream()
                .filter(b -> title.equals(b.getTitle()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        book.setUser(null);
        user.removeBook(book);
        userService.saveUser(user);
    }
}





















