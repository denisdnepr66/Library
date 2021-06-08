package com.denys.library.some.repositories;

import com.denys.library.some.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);
    List<Book> findByYearPublished(int yearPublished);
    List<Book> findByAuthor(String author);

}
