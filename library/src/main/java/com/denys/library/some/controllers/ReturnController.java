package com.denys.library.some.controllers;

import com.denys.library.some.models.Book;
import com.denys.library.some.models.User;
import com.denys.library.some.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/return")
public class ReturnController {

    @Autowired
    UserService userService;

    @PutMapping("/title={title}")
    public ResponseEntity<?> returnBook(@PathVariable String title){
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
        return new ResponseEntity<>(HttpStatus.OK);
    }
}





















