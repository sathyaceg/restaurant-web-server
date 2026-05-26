package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/* DTO object we get in API request */

public class Restaurant {

    public String city;
    public String name;

    @JsonProperty("estimated_cost")
    public Integer estimatedCost;

    @JsonProperty("user_rating")
    public UserRating userRating;

    public Integer id;

    public static class UserRating {
        @JsonProperty("average_rating")
        public Double averageRating;

        public Integer votes;
    }
}
