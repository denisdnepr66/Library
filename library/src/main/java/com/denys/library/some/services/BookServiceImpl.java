package com.denys.library.some.services;

import com.denys.library.some.DTO.BookDTO;
import com.denys.library.some.DTO.BookDTOForLibrarian;
import com.denys.library.some.DTO.BookDTOForReader;
import com.denys.library.some.exceptions.book.BookBadRequestException;
import com.denys.library.some.exceptions.book.BookNotFoundException;
import com.denys.library.some.models.Book;
import com.denys.library.some.models.User;
import com.denys.library.some.repositories.BookRepository;
import com.denys.library.some.repositories.UserRepository;
import com.denys.library.some.security.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveBook(Book book) {
        log.info("IN BookServiceImpl saveBook {}", book);
//        for (Book b: bookRepository.findAll()) {
//            if (b.getTitle().equals(book.getTitle())){
//                throw new BookBadRequestException("Book with this title already exist");
//            }
//        }
        bookRepository.save(book);
    }
    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(NoSuchElementException::new);
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
    public List<BookDTO> listBooks(){
        List<BookDTO> bookDTOS = new ArrayList<>();
        if (isLibrarian())
            bookRepository.findAll().forEach(book -> bookDTOS.add(new BookDTOForLibrarian(book)));
        else
            bookRepository.findAll().forEach(book
                    -> bookDTOS.add(new BookDTOForReader(book)));
        return bookDTOS;
    }

    @Override
    public BookDTO findBookById(Long id){
        if (id <= 0){
            throw new BookBadRequestException("Id cannot be less than 1");
        }
        if (!checkForExistingId(id)) {
            throw new BookNotFoundException("There is no book with id " + id);
        }
        BookDTO bookDTO;
        if (isLibrarian())
            bookDTO = new BookDTOForLibrarian(bookRepository.getById(id));
        else
            bookDTO = new BookDTOForReader(bookRepository.getById(id));
        return bookDTO;
    }

    @Override
    public void deleteBookById(Long id){
        if (id <= 0){
            throw new BookBadRequestException("Id cannot be less than 1");
        }
        if (!checkForExistingId(id)) {
            throw new BookNotFoundException("There is no book with id " + id);
        }
        bookRepository.deleteById(id);
    }


    @Override
    public void updateBook(Book book, Long id){
        if (id <= 0) {
            throw new BookBadRequestException("Id cannot be less than 1");
        }
        if (!checkForExistingId(id)) {
            throw new BookNotFoundException("There is no book with id " + id);
        }
        for (Book b: bookRepository.findAll()) {
            if (b.getTitle().equals(book.getTitle())){
                throw new BookBadRequestException("Book with this title already exist");
            }
        }
        try {
            book.setId(id);
            bookRepository.save(book);
        } catch (NoSuchElementException e) {
            throw new BookNotFoundException("No such book");
        }
    }

    @Override
    public List<BookDTO> findByAuthor(String author) {
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
    public List<BookDTO> findByYearPublished(int yearPublished) {
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
    public List<BookDTO> findByShortDescription(String shortDescription) {
        List<Book> foundDescription = new ArrayList<>();

        for (Book book1 : bookRepository.findAll()) {
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
    public List<BookDTO> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();
        bookRepository.findAll().forEach(book -> {
            if (book.getUser() == null) availableBooks.add(book);
        });

        List<BookDTO> bookDTOS = new ArrayList<>();
        if (isLibrarian())
            availableBooks.forEach(book -> bookDTOS.add(new BookDTOForLibrarian(book)));
        else
            availableBooks.forEach(book -> bookDTOS.add(new BookDTOForReader(book)));
        return bookDTOS;
    }

    private boolean checkForExistingId(Long id){
        return bookRepository.findAll()
                .stream()
                .anyMatch(o -> o.getId().equals(id));
    }


    public boolean isLibrarian() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        return user.getRole() == Role.LIBRARIAN;
    }

}
