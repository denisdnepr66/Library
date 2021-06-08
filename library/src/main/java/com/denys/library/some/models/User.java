package com.denys.library.some.models;

import com.denys.library.some.security.Role;
import com.denys.library.some.security.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<Book> books;

    @ManyToMany
    private List<Book> requestedBooks;


    public void addBook(Book book){
        books.add(book);
    }

    public void removeBook(){
        books.removeIf(book -> (LocalDate.now().getDayOfYear() - book.getDayTaken().getDayOfYear()) > 30);
    }

    public void removeBook(Book book){
        books.remove(book);
    }

    public void removeAllBooks(){
        books.forEach(book -> book.setUser(null));
    }
    public void removeAllRequestedBooks(){
        requestedBooks.forEach(requestedBook -> requestedBook.setUser(null));
    }

    public void addRequestedBook(Book book){
        requestedBooks.add(book);
    }

    public void removeRequestedBook(Book book){
        requestedBooks.remove(book);
    }

}