package com.denys.library.some.services;


import com.denys.library.some.DTO.GetUserBooksDTO;
import com.denys.library.some.DTO.UserDTO;
import com.denys.library.some.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(User user);

    List<UserDTO> listUsers();

    UserDTO getUserById(Long id);

    void deleteUser(Long id);

    Optional<User> findByEmail(String email);

//    void addUser(User user);

    void updateUser(User user, Long id);

    void giveRequestedBooksToUser(Long id);

    void giveAllRequestedBooks();

    GetUserBooksDTO getUserBooks(Long id);
}
