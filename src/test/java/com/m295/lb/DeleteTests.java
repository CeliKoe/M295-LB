package com.m295.lb;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeleteTest {

    //Delete one Book with BookId Endpoint
    private static final String SERVICE_URL_DELETE = "http://localhost:8080/artifact/resources/library/153";

    @Test
    public void givenDeleteOneBook_whenCorrectRequest_thenResponseCodeNoContent() throws IOException {
        HttpDelete request = new HttpDelete(SERVICE_URL_DELETE);

        // Authentifizierung einfügen
        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));

        // Ausführen des Requests
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = httpClient.execute(request);

        // Überprüfen, ob die Statuscode 204 (SC_NO_CONTENT) ist
        assertEquals(HttpStatus.SC_NO_CONTENT, httpResponse.getStatusLine().getStatusCode());
    }


    //Delete All Books Endpoint
    private static final String SERVICE_URL_ALL = "http://localhost:8080/artifact/resources/library/all";

    //Positive Test
    @Test
    public void givenBooksExist_whenDeleteAllBooks_thenResponseCodeNoContent() throws IOException {
        HttpDelete request = new HttpDelete(SERVICE_URL_ALL);

        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = httpClient.execute(request);

        assertEquals(HttpStatus.SC_NO_CONTENT, httpResponse.getStatusLine().getStatusCode());
    }

    //Negative Test
    @Test
    public void givenNoBooksExist_whenDeleteAllBooks_thenResponseCodeNotFound() throws IOException {
        HttpDelete request = new HttpDelete(SERVICE_URL_ALL);

        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = httpClient.execute(request);

        assertEquals(HttpStatus.SC_NOT_FOUND, httpResponse.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(httpResponse.getEntity()).contains("No books available to delete."));
    }

    //Test of the boundary conditions
}
