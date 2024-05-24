package com.m295.lb;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetTests {

    //Read data records with BookID Endpoint
    //Positive Test
    @Test
    public void givenGetModules_whenCorrectRequest_thenResponseCodeSuccess()
            throws IOException {

        HttpUriRequest request = new HttpGet("http://localhost:8080/artifact/resources/library/157");

        HttpResponse httpResponse = HttpClientBuilder
                .create()
                .build()
                .execute(request);

        assertEquals(httpResponse
                .getStatusLine()
                .getStatusCode(), HttpStatus.SC_OK);
    }

    //Negative Test
    @Test
    public void givenGetBook_whenBookDoesNotExist_thenResponseCodeNotFound() throws IOException {
        // URL for Book data not existing
        HttpUriRequest request = new HttpGet("http://localhost:8080/artifact/resources/library/999999");

        HttpResponse httpResponse = HttpClientBuilder
                .create()
                .build()
                .execute(request);

        // Should get HTTP Status 404 NOT FOUND
        assertEquals(HttpStatus.SC_NOT_FOUND, httpResponse.getStatusLine().getStatusCode());
    }

    //Read all data records endpoint
    //Positive Test
    @Test
    public void givenGetAllBooks_whenCorrectRequest_thenResponseCodeSuccess()
            throws IOException {

        // URL to get all books
        HttpUriRequest request = new HttpGet("http://localhost:8080/artifact/resources/library/all");

        HttpResponse httpResponse = HttpClientBuilder
                .create()
                .build()
                .execute(request);

        // Should get Response-Statuscode 200 OK
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
    }

    //Negative Test
    @Test
    public void givenPostRequestToGetAllBooks_whenMethodNotAllowed_thenResponseCodeMethodNotAllowed() throws IOException {
        // Erstellen eines POST-Request an einen Endpunkt, der nur für GET-Anfragen konfiguriert ist
        HttpUriRequest request = new HttpPost("http://localhost:8080/artifact/resources/library/all");

        HttpResponse httpResponse = HttpClientBuilder
                .create()
                .build()
                .execute(request);

        // Should get Statuscode 405 Method Not Allowed
        assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, httpResponse.getStatusLine().getStatusCode());
    }

    //Check if Book exists enpoint
    //Positive test
    @Test
    public void givenBookExists_whenCheckBookExists_thenResponseCodeOK() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:8080/artifact/resources/library/exists/159");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
    }

    //Negative Test
    @Test
    public void givenBookDoesNotExist_whenCheckBookExists_thenResponseCodeNotFound() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:8080/artifact/resources/library/exists/99999");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_NOT_FOUND, httpResponse.getStatusLine().getStatusCode());
    }

    //Count all books endpoint
    //Positive Test
    @Test
    public void givenBooksExist_whenCountBooks_thenResponseCodeOKAndCorrectCount() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:8080/artifact/resources/library/count");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
        long count = Long.parseLong(jsonResponse);

        assertTrue(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK && count >= 0);  // Counter can't be negativ
    }

    //Negative Test => None because it's not possible to test


    //Filter with Text and Date (Title, Publication Date) endpoint
    //Positive Test
    @Test
    public void givenValidTitle_whenSearchBooks_thenResponseCodeOKAndBooksReturned() throws IOException {
        String title = "1984";
        String url = "http://localhost:8080/artifact/resources/library/search?title=" + URLEncoder.encode(title, "UTF-8");

        HttpUriRequest request = new HttpGet(url);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

        String jsonOutput = EntityUtils.toString(httpResponse.getEntity());
        assertTrue(jsonOutput.contains(title));
    }

    //Negative Test
    @Test
    public void givenInvalidTitle_whenSearchBooks_thenResponseCodeOKAndNoBooksReturned() throws IOException {
        String title = "NonExistentTitle";  // Title not in DB
        String url = "http://localhost:8080/artifact/resources/library/search?title=" + URLEncoder.encode(title, "UTF-8");

        HttpUriRequest request = new HttpGet(url);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Right Statuscode?
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

        String jsonOutput = EntityUtils.toString(httpResponse.getEntity());
        assertTrue(jsonOutput.equals("[]"));  // Gets empty list
    }
}
