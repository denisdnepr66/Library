package com.denys.library.some.services;

import com.denys.library.some.DTO.BookDTO;
import com.denys.library.some.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    void saveBook(Book book);

    Book getBookById(Long id);

    Optional<Book> findByTitle(String title);

    BookDTO findBookByTitle(String title);

    List<BookDTO> listBooks();

    BookDTO findBookById(Long id);

    void deleteBookById(Long id);

    void updateBook(Book book, Long id);

    List<BookDTO> findByAuthor(String author);
    List<BookDTO> findByYearPublished(int yearPublished);
    List<BookDTO> findByShortDescription(String shortDescription);
    List<BookDTO> getAvailableBooks();

}