package com.example.demo.data;

import com.example.demo.model.Restaurant;
import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RestaurantDataStore {

    private final ConcurrentHashMap<String, Restaurant> restaurantsById = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> restaurantIdByCityAndName = new ConcurrentHashMap<>();

    public void addRestaurant(Restaurant restaurant) {
        String id = UuidCreator.getTimeOrderedEpoch().toString();
        String cityNameKey = cityNameKey(restaurant.city, restaurant.name);

        String existingId = restaurantIdByCityAndName.putIfAbsent(cityNameKey, id);
        if (existingId != null) {
            throw new IllegalArgumentException("Restaurant already exists for city and name");
        }

        restaurantsById.put(id, restaurant);
    }

    public Restaurant getRestaurantById(String id) {
        return restaurantsById.get(id);
    }

    public Restaurant getRestaurantByCityNameKey(String cityName, String restaurantName) {
        String restaurantId = restaurantIdByCityAndName.get(cityNameKey(cityName, restaurantName));
        return restaurantsById.get(restaurantId);
    }

    private String cityNameKey(String city, String name) {
        return normalize(city) + "|" + normalize(name);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
