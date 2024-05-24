package com.m295.lb.services;

import com.m295.lb.repos.BookRepository;
import com.m295.lb.repos.LendingRepository;
import com.m295.lb.models.Book;
import com.m295.lb.models.Lending;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
    public Response getAllBooks() {
        log.info("Fetching all books");
        try {
            List<Book> books = bookRepository.findAll();
            return Response.ok(books).build();
        } catch (Exception e) {
            log.error("Failed to fetch all books", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while fetching all books.")
                    .build();  // Return a server error response.
        }
    }

    //Read data records with BookID
    @PermitAll
    @GET
    @Path("/{bookId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getBook(@PathParam("bookId") int bookId) {
        log.info("Fetching Book with ID: {}", bookId);
        try {
            Optional<Book> book = bookRepository.findById(bookId);
            if (book.isPresent()) {
                return Response.ok(book.get()).build();
            } else {
                log.warn("No book found with ID: {}", bookId);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No book found with the provided ID.")
                        .build();
            }
        } catch (Exception e) {
            log.error("Error fetching book with ID: {}", bookId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while fetching the book.")
                    .build();
        }
    }

    //Data record existing (with ID)
    @PermitAll
    @GET
    @Path("/exists/{bookId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response checkBookExists(@PathParam("bookId") int bookId) {
        log.info("Checking existence for bookId: {}", bookId);
        boolean exists = bookRepository.existsById(bookId);

        if (exists) {
            log.info("Book with bookId: {} exists.", bookId);
            return Response.ok().build();
        } else {
            log.warn("Book with bookId: {} not found.", bookId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    //Determine number of data records
    @PermitAll
    @GET
    @Path("/count")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response countBooks() {
        log.info("Attempting to count all books");
        try {
            long count = bookRepository.count();
            log.info("Successfully counted all books: {}", count);
            return Response.ok(count).build();
        } catch (Exception e) {
            log.error("Error counting books", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while counting the books.")
                    .build();
        }
    }

    //Filter with Text and date(Title, Publication Date) //Title only works
    @PermitAll
    @GET
    @Path("/search")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Book> getBooksByTitleOrPublicationDate(@QueryParam("title") String title, @QueryParam("publicationDate") String publicationDateString) {
        List<Book> books = new ArrayList<>();
        Date publicationDate = null;
        if (publicationDateString != null) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                publicationDate = (Date) formatter.parse(publicationDateString);
            } catch (ParseException | java.text.ParseException e) {
                log.error("Error parsing publication date '{}'", publicationDateString, e);
                return books; // return empty list or handle error appropriately
            }
        }

        if (title != null && publicationDate != null) {
            log.info("Searching for books with title '{}' and publication date '{}'", title, publicationDate);
            books = bookRepository.findByTitleContainingAndPublicationDate(title, publicationDate);
        } else if (title != null) {
            log.info("Searching for books with title: '{}'", title);
            books = bookRepository.findByTitleContaining(title);
        } else if (publicationDate != null) {
            log.info("Searching for books with publication date: '{}'", publicationDate);
            books = bookRepository.findByPublicationDate(publicationDate);
        } else {
            log.info("No search criteria provided, returning empty book list.");
        }
        return books;
    }

    //Create a new Book data record
    @RolesAllowed("LIBRARIAN")
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBook(@Valid Book book) { // Hinzufügen des @Valid Annotations zur Validierung
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
                book.setLendingId(lending);
            }
            Book savedBook = bookRepository.save(book);
            log.info("Successfully created book: {}", savedBook);
            return Response.status(Response.Status.CREATED).entity(savedBook).build();

        } catch (ConstraintViolationException e) {
            log.error("Validation error while creating the book", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            log.error("Error creating the book", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating the book: " + e.getMessage()).build();
        }
    }

    //Create Multiple Book data records
    @RolesAllowed("LIBRARIAN")
    @POST
    @Path("/createMultiple")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooks(@Valid List<Book> books) {
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

    //Update a Book data record
    @RolesAllowed("LIBRARIAN")
    @PUT
    @Path("/update/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("bookId") Integer bookId, @Valid Book book) {
        if (book == null) {
            log.error("Attempted to update a book, but the data was null");
            return Response.status(Response.Status.BAD_REQUEST).entity("Book data must not be null.").build();
        }

        Optional<Book> existingBook = bookRepository.findById(bookId);
        if (!existingBook.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Book with ID " + bookId + " not found.").build();
        }

        try {
            if (book.getLending() != null && book.getLending().getLendingId() != null) {
                Integer lendingId = book.getLending().getLendingId();
                Lending lending = lendingRepository.findById(lendingId).orElse(null);
                if (lending == null) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("No Lending found with ID: " + lendingId).build();
                }
                book.setLendingId(lending);
            }
            book.setBookId(bookId);
            Book updatedBook = bookRepository.save(book);
            log.info("Successfully updated book: {}", updatedBook);
            return Response.status(Response.Status.OK).entity(updatedBook).build();
        } catch (Exception e) {
            log.error("Error updating the book", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating the book: " + e.getMessage()).build();
        }
    }

    //Delete a data record with BookID
    @RolesAllowed("LIBRARIAN")
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
    @RolesAllowed("LIBRARIAN")
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

    public void setBookRepository(BookRepository mockRepository) {
    }
}
