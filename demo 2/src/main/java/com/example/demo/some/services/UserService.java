package com.example.demo.some.services;


import com.example.demo.some.DTO.GetUserBooksDTO;
import com.example.demo.some.DTO.UserDTO;
import com.example.demo.some.models.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> listAllUsers();

    void saveUser(User user);

    User getUser(Long id);

    ResponseEntity<List<UserDTO>> listUsers();

    ResponseEntity<UserDTO> getUserById(Long id);

    ResponseEntity<UserDTO> deleteUser(Long id);

    Optional<User> findByEmail(String email);

    ResponseEntity<UserDTO> addUser(User user);

    ResponseEntity<UserDTO> updateUser(User user, Long id);

    void giveRequestedBooksToUser(Long id);

    void giveAllRequestedBooks();

    ResponseEntity<GetUserBooksDTO> getUserBooks(Long id);
}
