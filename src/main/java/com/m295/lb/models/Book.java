package com.m295.lb.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Book_ID")
    private Integer bookId;

    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters long.")
    @Column(name = "Title")
    private String title;

    @Column(name = "Author")
    private String author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @FutureOrPresent(message = "Publication date must be today or in the future.")
    @Column(name = "Publication_Date")
    private Date publicationDate;

    @Pattern(regexp = "^(Fiction|Non-Fiction|Science Fiction|Biography|History|Children)$", message = "Category must be one of the following: Fiction, Non-Fiction, Science Fiction, Biography, History, Children.")
    @Column(name = "Category")
    private String category;

    @Column(name = "Availability")
    private Boolean availability;

    @Column(name = "Price")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "Lending_ID")  // , nullable = true
    private Lending lending;

    public Book(){}

    public Lending getLending() {
        return lending;
    }

    public void setLendingId(Lending lending) {
        this.lending = lending;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
