package com.example.demo.service;

import com.example.demo.data.RestaurantDataStore;
import com.example.demo.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class RestaurantService {
    private final RestaurantDataStore restaurantDataStore;

    public RestaurantService(RestaurantDataStore restaurantDataStore) {
        this.restaurantDataStore = restaurantDataStore;
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurantDataStore.addRestaurant(restaurant);
    }

    public Restaurant getRestaurantByFilter(String city, Double budget) {
        List<Restaurant> candidates;

        if (city != null && budget != null) {
            candidates = restaurantDataStore.getRestaurantsByCityUnderBudget(city, budget);
        } else if (city != null) {
            candidates = restaurantDataStore.getRestaurantsByCity(city);
        } else {
            Stream<Restaurant> restaurantStream = restaurantDataStore.getAllRestaurants().stream();
            if (budget != null) {
                restaurantStream = restaurantStream.filter(restaurant -> restaurant.estimatedCost != null
                        && restaurant.estimatedCost <= budget);
            }
            candidates = restaurantStream.toList();
        }

        if (candidates.isEmpty()) {
            return null;
        }

        return candidates.stream()
                .filter(restaurant -> restaurant.userRating != null && restaurant.userRating.averageRating != null)
                .max(Comparator.comparing(restaurant -> restaurant.userRating.averageRating))
                .orElse(null);
    }
}
