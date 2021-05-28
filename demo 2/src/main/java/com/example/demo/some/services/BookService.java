package com.example.demo.some.services;

import com.example.demo.some.DTO.BookDTO;
import com.example.demo.some.models.Book;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> listAllBooks();

    void saveBook(Book book);

    Book getBook(Long id);

    void deleteBook(Long id);

    Optional<Book> findByTitle(String title);

    BookDTO findBookByTitle(String title);

    ResponseEntity<List<? extends BookDTO>> listBooks();

    ResponseEntity<BookDTO> findBookById(Long id);

    ResponseEntity<BookDTO> deleteBookById(Long id);

    ResponseEntity<Book> addBook(Book book);

    ResponseEntity<Book> updateBook(Book book, Long id);

    List<? extends BookDTO> findByAuthor(String author);
    List<? extends BookDTO> findByYearPublished(int yearPublished);
    List<? extends BookDTO> findByShortDescription(String shortDescription);
    List<? extends BookDTO> getAvailableBooks();

}