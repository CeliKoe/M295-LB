package com.m295.lb;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PutTests {

    //Update one Book with BookId Endpoint
    //Positive Test
    @Test
    public void givenValidBookData_whenUpdateBook_thenSuccess() throws IOException {

        HttpPut request = new HttpPut("http://localhost:8080/artifact/resources/library/update/211");

        // JSON-Data
        String jsonBook = "{\"title\":\"Updated Title\",\"author\":\"Aldous Huxley\",\"publicationDate\":\"01.01.2020\",\"category\":\"Science Fiction\",\"availability\":true,\"price\":15.5,\"lending\":{\"lendingId\":1}}";
        StringEntity entity = new StringEntity(jsonBook, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    //Negative Test
    @Test
    public void givenInvalidBookId_whenUpdateBook_thenNotFound() throws IOException {

        HttpPut request = new HttpPut("http://localhost:8080/artifact/resources/library/update/99999");

        // JSON-Data
        String jsonBook = "{\"title\":\"Nonexistent Book\",\"author\":\"Nonexistent Author\",\"publicationDate\":\"01.01.2020\",\"category\":\"Science Fiction\",\"availability\":true,\"price\":15.5,\"lending\":{\"lendingId\":1}}";
        StringEntity entity = new StringEntity(jsonBook, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        // Statuscode 404 (Not Found)
        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusLine().getStatusCode());
    }
}
