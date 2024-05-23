package com.m295.lb.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;

import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Book_ID")
    private Integer bookId;

    @Column(name = "Title")
    private String title;

    @Column(name = "Author")
    private String author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    //@FutureOrPresent(message = "Date must be today or in the future.")
    @Column(name = "Publication_Date")
    private Date publicationDate;

    @Column(name = "Category")
    private String category;

    @Column(name = "Availability")
    private Boolean availability;

    @Column(name = "Price")
    //@DecimalMin(value = "0.01", message = "Price must be at least 0.01.")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "Lending_ID")  // , nullable = true nullable true wenn nicht jede Buchausleihe erforderlich ist
    private Lending lending;

    public Book(){
    }

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
