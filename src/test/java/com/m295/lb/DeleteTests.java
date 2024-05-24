package com.m295.lb;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DeleteTests {

    //Delete All Books Endpoint
    //Positive Test
    @Test
    public void givenBooksExist_whenDeleteAllBooks_thenSuccess() throws IOException {

        HttpDelete request = new HttpDelete("http://localhost:8080/artifact/resources/library/all");

        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
    }

    //Negative Test
    @Test
    public void givenNoBooksExist_whenDeleteAllBooks_thenNotFound() throws IOException {

        HttpDelete request = new HttpDelete("http://localhost:8080/artifact/resources/library/all");

        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusLine().getStatusCode());
    }

    //Delete one Book with BookId Endpoint
    //Positive Test
    @Test
    public void givenExistingBookId_whenDeleteBook_thenSuccess() throws IOException {

        HttpDelete request = new HttpDelete("http://localhost:8080/artifact/resources/library/203");

        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
    }

    //Negative Test
    @Test
    public void givenNonExistingBookId_whenDeleteBook_thenNotFound() throws IOException {

        HttpDelete request = new HttpDelete("http://localhost:8080/artifact/resources/library/99999");

        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusLine().getStatusCode());
    }
}
