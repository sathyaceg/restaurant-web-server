package com.example.demo.service;

import com.example.demo.data.RestaurantDataStore;
import com.example.demo.model.Restaurant;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {
    private final RestaurantDataStore restaurantDataStore;

    public RestaurantService(RestaurantDataStore restaurantDataStore) {
        this.restaurantDataStore = restaurantDataStore;
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurantDataStore.addRestaurant(restaurant);
    }

    public Restaurant getRestaurantByFilter(String name, String city) {
        return restaurantDataStore.getRestaurantByCityNameKey(city, name);
    }
}
