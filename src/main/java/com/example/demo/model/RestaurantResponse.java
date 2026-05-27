package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RestaurantResponse {

    public int page;

    @JsonProperty("per_page")
    public int perPage;

    @JsonProperty("total_pages")
    public int totalPages;

    public int total;

    @JsonProperty("data")
    public List<Restaurant> restaurants;
}
