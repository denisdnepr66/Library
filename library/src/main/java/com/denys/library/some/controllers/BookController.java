package com.denys.library.some.controllers;

import com.denys.library.some.DTO.BookDTO;
import com.denys.library.some.models.Book;
import com.denys.library.some.services.BookService;
import com.denys.library.some.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<? extends BookDTO>> listBooks() {
        return new ResponseEntity<>(bookService.listBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findBookById(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.findBookById(id), HttpStatus.OK);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        bookService.saveBook(book);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@RequestBody Book book, @PathVariable Long id) {
        bookService.updateBook(book, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/title={title}")
    public ResponseEntity<BookDTO> findByTitle(@PathVariable String title) {
        return new ResponseEntity<>(bookService.findBookByTitle(title), HttpStatus.OK);
    }

    @GetMapping("/author={author}")
    public ResponseEntity<List<BookDTO>> findByAuthor(@PathVariable String author) {
        return new ResponseEntity<>(bookService.findByAuthor(author), HttpStatus.OK);
    }

    @GetMapping("/yearPublished={yearPublished}")
    public ResponseEntity<List<BookDTO>> findByYearPublished(@PathVariable int yearPublished) {
        return new ResponseEntity<>(bookService.findByYearPublished(yearPublished), HttpStatus.OK);
    }

    @GetMapping("/shortDescription={shortDescription}")
    public ResponseEntity<List<BookDTO>> findByShortDescription(@PathVariable String shortDescription) {
        return new ResponseEntity<>(bookService.findByShortDescription(shortDescription), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookDTO>> getAvailableBooks() {
        return new ResponseEntity<>(bookService.getAvailableBooks(), HttpStatus.OK);
    }

}
