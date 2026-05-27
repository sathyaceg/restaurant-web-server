package com.example.demo.client;

import com.example.demo.model.RestaurantResponse;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ExternalRestaurantClient {
    private static final String FOOD_OUTLETS_URL = "https://jsonmock.hackerrank.com/api/food_outlets?page=";

    private final HttpClient httpClient;
    private final Gson gson;

    public ExternalRestaurantClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public RestaurantResponse fetchFoodOutletsPage(int page) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(FOOD_OUTLETS_URL + page))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), RestaurantResponse.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to fetch food outlets", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch food outlets", e);
        }
    }
}
