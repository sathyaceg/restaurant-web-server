package com.example.demo.data;

import com.example.demo.model.Restaurant;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantDataStoreTest {

    @Test
    void addRestaurantStoresData() {
        RestaurantDataStore dataStore = new RestaurantDataStore();

        dataStore.addRestaurant(createRestaurant("Omaha", "Aaira", 180));

        List<Restaurant> allRestaurants = dataStore.getAllRestaurants();
        assertEquals(1, allRestaurants.size());
        assertEquals("Aaira", allRestaurants.getFirst().name);
    }

    @Test
    void addRestaurantRejectsDuplicateCityAndNameCaseInsensitive() {
        RestaurantDataStore dataStore = new RestaurantDataStore();

        dataStore.addRestaurant(createRestaurant("Omaha", "Aaira", 180));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> dataStore.addRestaurant(createRestaurant(" omaha ", "aaira", 220))
        );

        assertEquals("Restaurant already exists for city and name", exception.getMessage());
    }

    @Test
    void getRestaurantsByCityIsCaseInsensitive() {
        RestaurantDataStore dataStore = new RestaurantDataStore();
        dataStore.addRestaurant(createRestaurant("Omaha", "Aaira", 180));
        dataStore.addRestaurant(createRestaurant("OMAHA", "Bistro", 90));
        dataStore.addRestaurant(createRestaurant("Austin", "Hilltop", 120));

        List<Restaurant> omahaRestaurants = dataStore.getRestaurantsByCity("  omaha  ");

        assertEquals(2, omahaRestaurants.size());
    }

    @Test
    void getRestaurantsByCityUnderBudgetReturnsOnlyCostAtOrBelowBudgetFloor() {
        RestaurantDataStore dataStore = new RestaurantDataStore();
        dataStore.addRestaurant(createRestaurant("Omaha", "Aaira", 180));
        dataStore.addRestaurant(createRestaurant("Omaha", "Bistro", 150));
        dataStore.addRestaurant(createRestaurant("Omaha", "Clover", 199));

        List<Restaurant> filtered = dataStore.getRestaurantsByCityUnderBudget("Omaha", 180.9);

        assertEquals(2, filtered.size());
        assertTrue(filtered.stream().allMatch(r -> r.estimatedCost <= 180));
    }

    private Restaurant createRestaurant(String city, String name, int estimatedCost) {
        Restaurant restaurant = new Restaurant();
        restaurant.city = city;
        restaurant.name = name;
        restaurant.estimatedCost = estimatedCost;
        return restaurant;
    }
}
