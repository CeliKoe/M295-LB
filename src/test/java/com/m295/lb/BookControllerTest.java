package com.m295.lb;

import com.m295.lb.repos.BookRepository;
import com.m295.lb.services.BookController;
import jakarta.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BookControllerTest {
    private BookRepository mockRepository;
    private BookController bookController;

    @Before
    public void setUp() {
        // Create Mock-Repository
        mockRepository = Mockito.mock(BookRepository.class);

        bookController = new BookController();
        // Mock-Repository in Controller
        bookController.setBookRepository(mockRepository);
    }

    //Testing boundary conditions
    //DB Error
    @Test
    public void givenDatabaseError_whenGetAllBooks_thenResponseCodeInternalServerError() throws Exception {
        // Configure Mocks
        when(mockRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Simulate Requests
        Response response = bookController.getAllBooks();

        // Response
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }
}

