package com.example.demo.data;

import com.example.demo.model.Restaurant;
import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RestaurantDataStore {

    private final ConcurrentHashMap<String, Restaurant> restaurantsById = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, NavigableMap<Integer, List<Restaurant>>> byCityAndCost = new ConcurrentHashMap<>();

    public void addRestaurant(Restaurant restaurant) {
        String cityKey = normalize(restaurant.city);
        int costKey = costValue(restaurant);

        NavigableMap<Integer, List<Restaurant>> cityCostMap =
                byCityAndCost.computeIfAbsent(cityKey, ignored -> new TreeMap<>());

        synchronized (cityCostMap) {
            boolean duplicateExists = cityCostMap.values().stream()
                    .flatMap(List::stream)
                    .anyMatch(existing -> normalize(existing.name).equals(normalize(restaurant.name)));
            if (duplicateExists) {
                throw new IllegalArgumentException("Restaurant already exists for city and name");
            }

            String id = UuidCreator.getTimeOrderedEpoch().toString();
            restaurantsById.put(id, restaurant);
            cityCostMap.computeIfAbsent(costKey, ignored -> new ArrayList<>()).add(restaurant);
        }
    }

    public Restaurant getRestaurantById(String id) {
        return restaurantsById.get(id);
    }

    public Restaurant getRestaurantByCityNameKey(String cityName, String restaurantName) {
        NavigableMap<Integer, List<Restaurant>> cityCostMap = byCityAndCost.get(normalize(cityName));
        if (cityCostMap == null) {
            return null;
        }

        synchronized (cityCostMap) {
            return cityCostMap.values().stream()
                    .flatMap(List::stream)
                    .filter(restaurant -> normalize(restaurant.name).equals(normalize(restaurantName)))
                    .findFirst()
                    .orElse(null);
        }
    }

    public List<Restaurant> getRestaurantsByCity(String cityName) {
        NavigableMap<Integer, List<Restaurant>> cityCostMap = byCityAndCost.get(normalize(cityName));
        if (cityCostMap == null) {
            return List.of();
        }

        synchronized (cityCostMap) {
            return cityCostMap.values().stream()
                    .flatMap(List::stream)
                    .toList();
        }
    }

    public List<Restaurant> getRestaurantsByCityUnderBudget(String cityName, Double budget) {
        if (budget == null) {
            return getRestaurantsByCity(cityName);
        }

        NavigableMap<Integer, List<Restaurant>> cityCostMap = byCityAndCost.get(normalize(cityName));
        if (cityCostMap == null) {
            return List.of();
        }

        int budgetCeiling = (int) Math.floor(budget);

        synchronized (cityCostMap) {
            return cityCostMap.headMap(budgetCeiling, true)
                    .values()
                    .stream()
                    .flatMap(List::stream)
                    .toList();
        }
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantsById.values().stream().toList();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private int costValue(Restaurant restaurant) {
        if (restaurant == null || restaurant.estimatedCost == null) {
            return Integer.MAX_VALUE;
        }
        return restaurant.estimatedCost;
    }
}
