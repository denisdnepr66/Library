package com.denys.library.some.services;

import com.denys.library.some.DTO.GetUserBooksDTO;
import com.denys.library.some.DTO.UserDTO;
import com.denys.library.some.exceptions.book.BookNotFoundException;
import com.denys.library.some.exceptions.user.UserBadRequestException;
import com.denys.library.some.exceptions.user.UserNotFoundException;
import com.denys.library.some.models.Book;
import com.denys.library.some.models.User;
import com.denys.library.some.repositories.BookRepository;
import com.denys.library.some.repositories.UserRepository;
import com.denys.library.some.security.Role;
import com.denys.library.some.security.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Override
    public void saveUser(User user) {
        log.info("IN UserServiceImpl saveUser {}", user);
//        if (checkForExistingEmail(user.getEmail())){
//            throw new UserBadRequestException("User with this email already exist");
//        }
        if (emailValidation(user.getEmail())) {
            user.setRole(Role.READER);
            user.setStatus(Status.ACTIVE);
            userRepository.save(user);
        } else {
            throw new UserBadRequestException("Email has wrong type");
        }
    }

    @Override
    public List<UserDTO> listUsers(){
        log.info("IN UserServiceImpl listUsers");
        List<UserDTO> userDTOS = new ArrayList<>();
        userRepository.findAll().forEach(user -> userDTOS.add(new UserDTO(user)));
        if (userDTOS.isEmpty()) {
            throw new UserNotFoundException("No users found");
        }
        return userDTOS;
    }

//    @Override
//    public User getUser(Long id) {
//        log.info("IN UserServiceImpl getUser {}", id);
//        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
//    }

//    @Override
//    public UserDTO getUserById(Long id){
//        log.info("IN UserServiceImpl getUserById {}", id);
//        try {
//            User user = userRepository.getById(id);
//            return new UserDTO(user);
//        } catch (NoSuchElementException e) {
//            throw new UserNotFoundException("There is no user with id " + id);
//        }
//    }

    @Override
    public UserDTO getUserById(Long id){
        log.info("IN UserServiceImpl getUserById {}", id);
        if (!checkForExistingId(id)) {
            throw new BookNotFoundException("There is no user with id " + id);
        }
        User user = userRepository.getById(id);
        return new UserDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("IN UserServiceImpl listAllUsers {}", id);

        if (id <= 0) {
            throw new UserBadRequestException("Id cannot be less than 1");
        }
        if (!checkForExistingId(id)) {
            throw new BookNotFoundException("There is no user with id " + id);
        }
        userRepository.getById(id).removeAllBooks();
        userRepository.getById(id).removeAllRequestedBooks();
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

//    @Override
//    public void addUser(User user){
//        if (checkForExistingEmail(user.getEmail())){
//            throw new UserBadRequestException("User with this email already exist");
//        }
//        if (emailValidation(user.getEmail())) {
//            user.setRole(Role.READER);
//            user.setStatus(Status.ACTIVE);
//            userRepository.save(user);
//        } else {
//            throw new UserBadRequestException("Email has wrong type");
//        }
//    }

    @Override
    public void updateUser(User user, Long id){
        if (id <= 0) {
            throw new UserBadRequestException("Id cannot be less than 1");
        }
        if (!checkForExistingId(id)) {
            throw new BookNotFoundException("There is no user with id " + id);
        }
        if (checkForExistingId(id)) {
            for (User u : userRepository.findAll()) {
                if (u.getEmail().equals(user.getEmail()) && !id.equals(u.getId())) {
                    throw new UserBadRequestException("Input email already exist");
                }
            }
        } else
            throw new UserNotFoundException("There is no user with id " + id);

        if (emailValidation(user.getEmail())){
            user.setId(id);
            user.setRole(Role.READER);
            user.setStatus(Status.ACTIVE);
            userRepository.save(user);
        }
        else
            throw new UserBadRequestException("Email has wrong type");
    }

    @Override
    public void giveRequestedBooksToUser(Long id){
        if (id <= 0) {
            throw new UserBadRequestException("Id cannot be less than 1");
        }
        if (!checkForExistingId(id)) {
            throw new BookNotFoundException("There is no user with id " + id);
        }
        User user = userRepository.getById(id);
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
            bookRepository.save(book);
        }
        userRepository.save(user);
    }

    @Override
    public void giveAllRequestedBooks(){
        List<User> users = userRepository.findAll();

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
            userRepository.save(user);
        });
    }

    @Override
    public GetUserBooksDTO getUserBooks(Long id){
        if (id <= 0) {
            throw new UserBadRequestException("Id cannot be less than 1");
        }
        if (!checkForExistingId(id)) {
            throw new BookNotFoundException("There is no user with id " + id);
        }
        User user = userRepository.getById(id);
        return new GetUserBooksDTO(user);
    }

    private boolean checkForExistingId(Long id){
        return userRepository.findAll()
                .stream()
                .anyMatch(o -> o.getId().equals(id));
    }

    private boolean checkForExistingEmail(String email){
        return userRepository.findAll()
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
        for (User user : userRepository.findAll()) {
            user.removeBook();
        }
    }
}
