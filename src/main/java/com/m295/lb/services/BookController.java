package com.m295.lb.services;

import com.m295.lb.BookRepository;
import com.m295.lb.LendingRepository;
import com.m295.lb.models.Book;
import com.m295.lb.models.Lending;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/library")
@Slf4j
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LendingRepository lendingRepository;

    //Read all data records
    @PermitAll
    @GET
    @Path("/all")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    //Read data records with BookID
    @PermitAll
    @GET
    //@Valid
    @Path("/{bookId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Optional<Book> getBook(@PathParam("bookId") int bookId) {
        log.info("Fetching Book with ID: {}", bookId);
        return bookRepository.findById(bookId);
    }

    //Data record existing (with ID)
    @PermitAll
    @GET
    @Path("/exists/{bookId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response checkBookExists(@PathParam("bookId") int bookId) {
        boolean exists = bookRepository.existsById(bookId);
        return exists ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    //Determine number of data records
    @PermitAll
    @GET
    @Path("/count")
    @Produces({ MediaType.APPLICATION_JSON })
    public Long countBooks() {
        return bookRepository.count();
    }

    //Filter with Text (Title, Author) return von title data record not working
    @PermitAll
    @GET
    @Path("/search")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Book> getBooksByTitleOrAuthor(@QueryParam("title") String title, @QueryParam("author") String author) {
        List<Book> books = new ArrayList<>();
        if (title != null && author != null) {
            // Suche nach Büchern, die sowohl den Titel als auch den Autor enthalten
            log.info("Searching for books with title '{}' and author '{}'", title, author);
            books = bookRepository.findByTitleContainingAndAuthorContaining(title, author);
        } else if (title != null) {
            // Suche nur nach Titel
            log.info("Searching for books with title: '{}'", title);
            books = bookRepository.findByTitleContaining(title);
        } else if (author != null) {
            // Suche nur nach Autor
            log.info("Searching for books with author: '{}'", author);
            books = bookRepository.findByAuthorContaining(author);
        } else {
            // Keine Parameter angegeben, leere Liste zurückgeben
            log.info("No search criteria provided, returning empty book list.");
        }
        return books;
    }

    //Filter with date not working!!!
    @PermitAll
    @GET
    @Path("/date/{publicationDate}")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Book> getBooksByDate(@PathParam("publicationDate") String publicationDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate date = LocalDate.parse(publicationDate, formatter);
            return bookRepository.findByPublicationDate(Date.valueOf(date).toLocalDate());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Das Datum muss im Format dd.MM.yyyy sein.");
        }
    }

    //Create a new Book data record
    // @RolesAllowed("ADMIN")
    @PermitAll
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBook(Book book) {
        if (book == null) {
            log.error("Attempted to create a book, but the data was null");
            return Response.status(Response.Status.BAD_REQUEST).entity("Book data must not be null.").build();
        }
        try {
            if (book.getLending() != null && book.getLending().getLendingId() != null) {
                Integer lendingId = book.getLending().getLendingId();
                Optional<Lending> lendingOptional = lendingRepository.findById(lendingId);
                Lending lending = lendingOptional.orElse(null);

                if (lending == null) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("No Lending found with ID: " + lendingId).build();
                }
                book.setLendingId(lending); // Korrigierte Methode verwenden
            }
            Book savedBook = bookRepository.save(book);
            log.info("Successfully created book: {}", savedBook);
            return Response.status(Response.Status.CREATED).entity(savedBook).build();
        } catch (Exception e) {
            log.error("Error creating the book", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating the book: " + e.getMessage()).build();
        }
    }

    //Create Multiple Book data records
    @PermitAll
    @POST
    @Path("/createMultiple")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooks(List<Book> books) {
        if (books == null || books.isEmpty()) {
            log.error("Attempted to create books, but the data was null or empty");
            return Response.status(Response.Status.BAD_REQUEST).entity("Book data must not be null or empty.").build();
        }

        List<Book> savedBooks = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (Book book : books) {
            try {
                if (book.getLending() != null && book.getLending().getLendingId() != null) {
                    Integer lendingId = book.getLending().getLendingId();
                    Lending lending = lendingRepository.findById(lendingId).orElse(null);

                    if (lending == null) {
                        errors.add("No Lending found with ID: " + lendingId);
                        continue; // Skip saving this book
                    }
                    book.setLendingId(lending);
                }

                Book savedBook = bookRepository.save(book);
                savedBooks.add(savedBook);
                log.info("Successfully created book: {}", savedBook);
            } catch (Exception e) {
                log.error("Error creating the book: " + book.getTitle(), e);
                errors.add("Error creating the book: " + book.getTitle() + " - " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
        }

        return Response.status(Response.Status.CREATED).entity(savedBooks).build();
    }

    //Delete a data record with BookID
    //@RolesAllowed("ADMIN")
    @PermitAll
    @DELETE
    @Path("/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("bookId") int bookId) {

        log.info("Requested deletion for book ID: {}", bookId);

        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            log.info("Successfully deleted book ID: {}", bookId);
            return Response.status(Response.Status.NO_CONTENT).build();

        } else {
            log.error("Deletion failed: No book found with ID {}", bookId);
            return Response.status(Response.Status.NOT_FOUND).entity("Book with ID " + bookId + " not found.").build();
        }
    }

    //Delete all data records
    //@RolesAllowed("ADMIN")
    @PermitAll
    @DELETE
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllBooks() {
        log.info("Requested deletion of all books");

        try {
            // Check whether there are books to delete
            List<Book> books = bookRepository.findAll();
            if (books.isEmpty()) {
                log.info("No books to delete.");
                return Response.status(Response.Status.NOT_FOUND).entity("No books available to delete.").build();
            }
            // Deleting all books
            bookRepository.deleteAll();
            log.info("Successfully deleted all books");
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            log.error("Error deleting books", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while deleting all books: " + e.getMessage()).build();
        }
    }

}
