package com.m295.lb;

import com.m295.lb.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LibraryRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthorContaining(String author);

    List<Book> findByPublicationDate(LocalDate date);

    List<Book> findByTitleContainingAndAuthorContaining(String title, String author);
}
