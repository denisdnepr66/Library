package com.denys.library.some.controllers;

import com.denys.library.some.DTO.GetUserBooksDTO;
import com.denys.library.some.DTO.UserDTO;
import com.denys.library.some.models.Book;
import com.denys.library.some.models.User;
import com.denys.library.some.services.BookService;
import com.denys.library.some.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> listUsers() {
        return new ResponseEntity<>(userService.listUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable Long id) {
        userService.updateUser(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("{id}/books")
    public ResponseEntity<GetUserBooksDTO> getBooksByUserId(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserBooks(id), HttpStatus.OK);
    }

    @PostMapping("/giveRequestedBooks/{id}")
    public ResponseEntity<?> giveRequestedBookToUser(@PathVariable Long id){
        userService.giveRequestedBooksToUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/giveAllRequestedBooks")
    public ResponseEntity<?> giveAllRequestedBooks(){
        userService.giveAllRequestedBooks();
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PostMapping("/")
//    public ResponseEntity<?> giveBookToUser(Long user_id, Long book_id){
//        UserDTO user = userService.getUserById(user_id);
//        Book book = bookService.getBookById(book_id);
//        try {
//            if (book.getUser() == null) {
//                if (user.getRequestedBooks().contains(book.getTitle())){
//                    user.getRequestedBooks().remove(book.getTitle());
//                    user.getBooks().add(book.getTitle());
//                    book.setUser(user);
//                }
//            }
//        } catch (NullPointerException npe){
//            npe.printStackTrace();
//        }
//    }
}
