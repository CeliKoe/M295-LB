package com.m295.lb;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
//PW und Username ändern
public class DeleteTest {
//Link ändern
    private static final String SERVICE_URL = "http://localhost:8080/artifact/resources/books/3";

    @Test
    public void givenDeleteModule_whenCorrectRequest_thenResponseCodeNoContent() throws IOException {
        HttpDelete request = new HttpDelete(SERVICE_URL);

        // Authentifizierung einfügen
        request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:1234".getBytes()));

        // Ausführen des Requests
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = httpClient.execute(request);

        // Überprüfen, ob die Statuscode 204 (SC_NO_CONTENT) ist
        assertEquals(HttpStatus.SC_NO_CONTENT, httpResponse.getStatusLine().getStatusCode());
    }
}
