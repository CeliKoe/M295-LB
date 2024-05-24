package com.m295.lb;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostTests {

    //Create Multiple Book data records Endpoint
    //Positive Test
    @Test
    public void givenValidBookData_whenCreateBook_thenSuccess() throws IOException {

        HttpPost request = new HttpPost("http://localhost:8080/artifact/resources/library/create");

        // JSON-Data
        String jsonBook = "{\"title\":\"Testing Post Test 2\",\"author\":\"Aldous Huxley\",\"publicationDate\":\"01.01.2020\",\"category\":\"Science Fiction\",\"availability\":false,\"price\":12.5,\"lending\":{\"lendingId\":1}}";
        StringEntity entity = new StringEntity(jsonBook, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_CREATED, response.getStatusLine().getStatusCode());
    }

    //Negative Test
    @Test
    public void givenInvalidCategory_whenCreateBook_thenBadRequest() throws IOException {

        HttpPost request = new HttpPost("http://localhost:8080/artifact/resources/library/create");

        // JSON-Data
        String jsonBook = "{\"title\":\"Testing Post Test Negative\",\"author\":\"Aldous Huxley\",\"publicationDate\":\"01.01.2020\",\"category\":\"Invalid\",\"availability\":false,\"price\":12.5,\"lending\":{\"lendingId\":1}}";
        StringEntity entity = new StringEntity(jsonBook, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
    }

    //Create a new Book data record
    //Positive Test
    @Test
    public void givenValidBooksData_whenCreateMultipleBooks_thenSuccess() throws IOException {

        HttpPost request = new HttpPost("http://localhost:8080/artifact/resources/library/createMultiple");

        // JSON-Data for multiple Books
        String jsonBooks = "[" +
                "{\"title\":\"Testing Post Test 1\",\"author\":\"Author One\",\"publicationDate\":\"01.01.2020\",\"category\":\"Science Fiction\",\"availability\":true,\"price\":15.5,\"lending\":{\"lendingId\":1}}," +
                "{\"title\":\"Testing Post Test 2\",\"author\":\"Author Two\",\"publicationDate\":\"01.01.2021\",\"category\":\"Fiction\",\"availability\":false,\"price\":12.5,\"lending\":{\"lendingId\":2}}" +
                "]";
        StringEntity entity = new StringEntity(jsonBooks, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));
        request.setHeader("Content-type", "application/json");

        // HTTP POST Request
        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        // Should be Statuscode 201 (Created)
        assertEquals(HttpStatus.SC_CREATED, response.getStatusLine().getStatusCode());
    }

    //Negative Test
    @Test
    public void givenInvalidBooksData_whenCreateMultipleBooks_thenBadRequest() throws IOException {

        HttpPost request = new HttpPost("http://localhost:8080/artifact/resources/library/createMultiple");

        // JSON-Data with invalid category
        String jsonBooks = "[" +
                "{\"title\":\"Valid Book\",\"author\":\"Author Valid\",\"publicationDate\":\"01.01.2020\",\"category\":\"Science Fiction\",\"availability\":true,\"price\":15.5,\"lending\":{\"lendingId\":1}}," +
                "{\"title\":\"Invalid Book\",\"author\":\"Author Invalid\",\"publicationDate\":\"01.01.2021\",\"category\":\"Unknown Genre\",\"availability\":false,\"price\":12.5,\"lending\":{\"lendingId\":2}}" +
                "]";
        StringEntity entity = new StringEntity(jsonBooks, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
    }

}
