package com.varunu28.scratchpad.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpClientDemo {

    private static final String URL = "https://api.thecatapi.com/v1/images/search?limit=10";

    static void main() {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            List<Image> images = objectMapper.readValue(response.body(),
                objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, Image.class));

            images.forEach(System.out::println);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    record Image(String id, String url, int width, int height) {}
}
