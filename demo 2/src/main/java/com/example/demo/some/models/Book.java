package com.example.demo.some.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "year_published")
    private int yearPublished;

    @Column(name = "short_description")
    private String shortDescription;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "day_taken")
    private LocalDate dayTaken;

    @ManyToMany
    private List<User> usersInQueue;


    public void addUserToQueue(User user){
        usersInQueue.add(user);
    }

    public void removeUserFromQueue(User user){
        usersInQueue.remove(user);
    }

    public void removeAllUserBooks(){
        user.removeAllBooks();
    }

    public void removeAllUsersInQueue(){
        for (User user1 : usersInQueue) {
            user1.removeAllRequestedBooks();
        }
    }
}
