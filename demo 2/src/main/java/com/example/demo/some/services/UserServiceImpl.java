package com.example.demo.some.services;

import com.example.demo.some.DTO.GetUserBooksDTO;
import com.example.demo.some.DTO.UserDTO;
import com.example.demo.some.models.Book;
import com.example.demo.some.models.User;
import com.example.demo.some.repositories.UserRepository;
import com.example.demo.some.security.Role;
import com.example.demo.some.security.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Override
    public List<User> listAllUsers() {
        log.info("IN UserServiceImpl listAllUsers");
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        log.info("IN UserServiceImpl saveUser {}", user);
        userRepository.save(user);
    }

    @Override
    public User getUser(Long id) {
        log.info("IN UserServiceImpl listAllUsers {}", id);
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public ResponseEntity<List<UserDTO>> listUsers(){
        List<UserDTO> userDTOS = new ArrayList<>();
        userService.listAllUsers().forEach(user -> userDTOS.add(new UserDTO(user)));
        if (userDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long id){
        try {
            User user = userService.getUser(id);
            UserDTO userDTO = new UserDTO(user);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //TODO check delete method when the user have books and requested books
    @Override
    public ResponseEntity<UserDTO> deleteUser(Long id) {
        log.info("IN UserServiceImpl listAllUsers {}", id);

        if (id < 0 || checkForExistingId(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.getUser(id).removeAllBooks();
        userService.getUser(id).removeAllRequestedBooks();

        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public ResponseEntity<UserDTO> addUser(User user){
        if (checkForExistingEmail(user.getEmail())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (emailValidation(user.getEmail())) {
            user.setRole(Role.READER);
            user.setStatus(Status.ACTIVE);
            userService.saveUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(User user, Long id){
        if (id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (checkForExistingId(id)) {
            for (User u : userService.listAllUsers()) {
                if (u.getEmail().equals(user.getEmail()) && !id.equals(u.getId())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (emailValidation(user.getEmail())){
            user.setId(id);
            user.setRole(Role.READER);
            user.setStatus(Status.ACTIVE);
            userService.saveUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public void giveRequestedBooksToUser(Long id){
        User user = userService.getUser(id);
        List<Book> requestedBooks = user.getRequestedBooks();
        List<Book> temp = new ArrayList<>();
        requestedBooks.forEach(book -> {
            if (book.getUser() == null){
                temp.add(book);
            }
        });

        for (Book book : temp) {
            if (user.getBooks().size() == 3) {
                break;
            }
            user.addBook(book);
            book.setUser(user);
            book.setDayTaken(LocalDate.now());
            user.removeRequestedBook(book);
            book.removeUserFromQueue(user);
            bookService.saveBook(book);
        }
        userService.saveUser(user);
    }

    @Override
    public void giveAllRequestedBooks(){
        List<User> users = userService.listAllUsers();

        users.forEach(user -> {
            List<Book> requestedBooks = user.getRequestedBooks();
            List<Book> temp = new ArrayList<>();
            requestedBooks.forEach(book -> {
                if (book.getUser() == null){
                    temp.add(book);
                }
            });

            for (Book book : temp) {
                if (user.getBooks().size() == 3) {
                    break;
                }
                user.addBook(book);
                book.setUser(user);
                book.setDayTaken(LocalDate.now());
                user.removeRequestedBook(book);
            }
            userService.saveUser(user);
        });
    }

    @Override
    public ResponseEntity<GetUserBooksDTO> getUserBooks(Long id){
        if (id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getUser(id);

        try {
            return new ResponseEntity<>(new GetUserBooksDTO(user), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private boolean checkForExistingId(Long id){
        return userService.listAllUsers()
                .stream()
                .anyMatch(o -> o.getId().equals(id));
    }

    private boolean checkForExistingEmail(String email){
        return userService.listAllUsers()
                .stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }

    private boolean emailValidation(String email){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void checkIfBookExpired_30_days() {
        for (User user : userService.listAllUsers()) {
            user.removeBook();
        }
    }
}
