package com.m295.lb.repos;

import com.m295.lb.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitleContainingAndPublicationDate(String title, Date publicationDate);
    List<Book> findByTitleContaining(String title);
    List<Book> findByPublicationDate(Date publicationDate);
}
