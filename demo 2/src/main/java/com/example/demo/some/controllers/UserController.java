package com.example.demo.some.controllers;

import com.example.demo.some.DTO.GetUserBooksDTO;
import com.example.demo.some.DTO.UserDTO;
import com.example.demo.some.models.User;
import com.example.demo.some.services.BookService;
import com.example.demo.some.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return userService.listUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody User user, @PathVariable Long id) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }


    @GetMapping("{id}/books")
    public ResponseEntity<GetUserBooksDTO> getBooksByUserId(@PathVariable Long id){
        return userService.getUserBooks(id);
    }

    @PutMapping("/giveRequestedBooks/{id}")
    public void giveRequestedBookToUser(@PathVariable Long id){
        userService.giveRequestedBooksToUser(id);

    }

    @PutMapping("/giveAllRequestedBooks")
    public void giveAllRequestedBooks(){
        userService.giveAllRequestedBooks();
    }
}
