package com.example.demo.some.services;

import com.example.demo.some.DTO.BookDTO;
import com.example.demo.some.DTO.BookDTOForLibrarian;
import com.example.demo.some.DTO.BookDTOForReader;
import com.example.demo.some.models.Book;
import com.example.demo.some.models.User;
import com.example.demo.some.repositories.BookRepository;
import com.example.demo.some.security.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Autowired
    private UserService userService;

    @Override
    public List<Book> listAllBooks() {
        log.info("IN BookServiceImpl listAllBooks");
        return bookRepository.findAll();
    }

    @Override
    public void saveBook(Book book) {
        log.info("IN BookServiceImpl saveBook {}", book);
        bookRepository.save(book);
    }

    @Override
    public Book getBook(Long id) {
        log.info("IN BookServiceImpl getBook {}", id);
        return bookRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void deleteBook(Long id) {
        log.info("IN BookServiceImpl deleteBook {}", id);
        bookRepository.deleteById(id);
    }

    @Override
    public Optional<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public BookDTO findBookByTitle(String title) {
        if (isLibrarian()) {
            return new BookDTOForLibrarian(bookRepository.findByTitle(title).orElseThrow(NoClassDefFoundError::new));
        } else {
            return new BookDTOForReader(bookRepository.findByTitle(title).orElseThrow(NoClassDefFoundError::new));
        }
    }

    @Override
    public ResponseEntity<List<? extends BookDTO>> listBooks(){
        List<BookDTO> bookDTOS = new ArrayList<>();
        if (isLibrarian())
            bookService.listAllBooks().forEach(book
                    -> bookDTOS.add(new BookDTOForLibrarian(book)));
        else
            bookService.listAllBooks().forEach(book
                    -> bookDTOS.add(new BookDTOForReader(book)));

        return new ResponseEntity<>(bookDTOS, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookDTO> findBookById(Long id){
        if (id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            BookDTO bookDTO;
            if (isLibrarian())
                bookDTO = new BookDTOForLibrarian(bookService.getBook(id));
            else
                bookDTO = new BookDTOForReader(bookService.getBook(id));

            return new ResponseEntity<>(bookDTO, HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<BookDTO> deleteBookById(Long id){
        if (id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!bookService.listAllBooks().contains(bookService.getBook(id))) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Book> addBook(Book book){
        for (Book b: bookService.listAllBooks()) {
            if (b.getTitle().equals(book.getTitle())){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        bookService.saveBook(book);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Book> updateBook(Book book, Long id){
        if (id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        for (Book b: bookService.listAllBooks()) {
            if (b.getTitle().equals(book.getTitle())){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        try {
            book.setId(id);
            bookService.saveBook(book);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<? extends BookDTO> findByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        List<BookDTO> bookDTOS = new ArrayList<>();
        if (isLibrarian()){
            books.forEach(book -> bookDTOS.add(new BookDTOForLibrarian(book)));
        } else {
            books.forEach(book -> bookDTOS.add(new BookDTOForLibrarian(book)));
        }
        return bookDTOS;
    }

    @Override
    public List<? extends BookDTO> findByYearPublished(int yearPublished) {
        List<Book> books = bookRepository.findByYearPublished(yearPublished);
        List<BookDTO> bookDTOS = new ArrayList<>();
        if (isLibrarian()){
            books.forEach(book -> bookDTOS.add(new BookDTOForLibrarian(book)));
        } else {
            books.forEach(book -> bookDTOS.add(new BookDTOForLibrarian(book)));
        }
        return bookDTOS;
    }

    @Override
    public List<? extends BookDTO> findByShortDescription(String shortDescription) {
        List<Book> foundDescription = new ArrayList<>();

        for (Book book1 : bookService.listAllBooks()) {
            boolean found = book1.getShortDescription().toLowerCase()
                    .contains(shortDescription.toLowerCase());
            if (found) {
                foundDescription.add(book1);
            }
        }

        List<BookDTO> bookDTOS = new ArrayList<>();
        if (isLibrarian())
            foundDescription.forEach(book -> bookDTOS.add(new BookDTOForLibrarian(book)));
        else
            foundDescription.forEach(book -> bookDTOS.add(new BookDTOForReader(book)));
        return bookDTOS;

    }

    @Override
    public List<? extends BookDTO> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();
        bookService.listAllBooks().forEach(book -> {
            if (book.getUser() == null) availableBooks.add(book);
        });

        List<BookDTO> bookDTOS = new ArrayList<>();
        if (isLibrarian())
            availableBooks.forEach(book -> bookDTOS.add(new BookDTOForLibrarian(book)));
        else
            availableBooks.forEach(book -> bookDTOS.add(new BookDTOForReader(book)));
        return bookDTOS;
    }


    public boolean isLibrarian() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        User user = userService.findByEmail(email).orElseThrow(NoSuchElementException::new);
        return user.getRole() == Role.LIBRARIAN;
    }

}
