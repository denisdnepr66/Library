package com.example.demo.some.controllers;

import com.example.demo.some.DTO.BookDTO;
import com.example.demo.some.DTO.BookDTOForLibrarian;
import com.example.demo.some.DTO.BookDTOForReader;
import com.example.demo.some.models.Book;
import com.example.demo.some.models.User;
import com.example.demo.some.security.Role;
import com.example.demo.some.services.BookService;
import com.example.demo.some.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<? extends BookDTO>> listBooks() {
        return bookService.listBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@RequestBody Book book, @PathVariable Long id) {
        return bookService.updateBook(book,id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDTO> deleteBookById(@PathVariable Long id) {
        return bookService.deleteBookById(id);
    }

    @GetMapping("/title={title}")
    public ResponseEntity<BookDTO> findByTitle(@PathVariable String title) {
        return new ResponseEntity<>(bookService.findBookByTitle(title), HttpStatus.OK);
    }

    @GetMapping("/author={author}")
    public ResponseEntity<List<? extends BookDTO>> findByAuthor(@PathVariable String author) {
        return new ResponseEntity<>(bookService.findByAuthor(author), HttpStatus.OK);
    }

    @GetMapping("/yearPublished={yearPublished}")
    public ResponseEntity<List<? extends BookDTO>> findByYearPublished(@PathVariable int yearPublished) {
        return new ResponseEntity<>(bookService.findByYearPublished(yearPublished), HttpStatus.OK);
    }

    @GetMapping("/shortDescription={shortDescription}")
    public ResponseEntity<List<? extends BookDTO>> findByShortDescription(@PathVariable String shortDescription) {
        return new ResponseEntity<>(bookService.findByShortDescription(shortDescription), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<? extends BookDTO>> getAvailableBooks() {
        return new ResponseEntity<>(bookService.getAvailableBooks(), HttpStatus.OK);
    }

}
