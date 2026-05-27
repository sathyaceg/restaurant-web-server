package com.example.demo.service;

import com.example.demo.client.ExternalRestaurantClient;
import com.example.demo.data.RestaurantDataStore;
import com.example.demo.model.Restaurant;
import com.example.demo.model.RestaurantResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantServiceTest {

    @Mock
    private RestaurantDataStore restaurantDataStore;

    @Mock
    private ExternalRestaurantClient externalRestaurantClient;

    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantService = new RestaurantService(restaurantDataStore, externalRestaurantClient);
    }

    @Test
    void getRestaurantByFilterUsesCityAndBudgetBranch() {
        Restaurant high = createRestaurant("Omaha", "A", 100, 4.8);
        Restaurant low = createRestaurant("Omaha", "B", 80, 4.1);
        when(restaurantDataStore.getRestaurantsByCityUnderBudget("Omaha", 120.0)).thenReturn(List.of(low, high));

        Restaurant result = restaurantService.getRestaurantByFilter("Omaha", 120.0);

        assertEquals("A", result.name);
        verify(restaurantDataStore).getRestaurantsByCityUnderBudget("Omaha", 120.0);
    }

    @Test
    void getRestaurantByFilterUsesCityOnlyBranch() {
        Restaurant r1 = createRestaurant("Omaha", "A", 100, 4.2);
        Restaurant r2 = createRestaurant("Omaha", "B", 80, 4.7);
        when(restaurantDataStore.getRestaurantsByCity("Omaha")).thenReturn(List.of(r1, r2));

        Restaurant result = restaurantService.getRestaurantByFilter("Omaha", null);

        assertEquals("B", result.name);
        verify(restaurantDataStore).getRestaurantsByCity("Omaha");
    }

    @Test
    void getRestaurantByFilterUsesGlobalBudgetBranch() {
        Restaurant underBudget = createRestaurant("Austin", "Cheap", 90, 4.0);
        Restaurant overBudget = createRestaurant("Austin", "Expensive", 200, 4.9);
        when(restaurantDataStore.getAllRestaurants()).thenReturn(List.of(underBudget, overBudget));

        Restaurant result = restaurantService.getRestaurantByFilter(null, 100.0);

        assertEquals("Cheap", result.name);
        verify(restaurantDataStore).getAllRestaurants();
    }

    @Test
    void getRestaurantByFilterReturnsNullWhenNoCandidateHasRating() {
        Restaurant noRating = new Restaurant();
        noRating.city = "Omaha";
        noRating.name = "NoRating";
        noRating.estimatedCost = 50;
        noRating.userRating = null;

        when(restaurantDataStore.getRestaurantsByCity("Omaha")).thenReturn(List.of(noRating));

        Restaurant result = restaurantService.getRestaurantByFilter("Omaha", null);

        assertNull(result);
    }

    @Test
    void fetchFoodOutletsPageDelegatesToExternalClient() {
        RestaurantResponse response = new RestaurantResponse();
        response.page = 10;
        when(externalRestaurantClient.fetchFoodOutletsPage(10)).thenReturn(response);

        RestaurantResponse result = restaurantService.fetchFoodOutletsPage(10);

        assertEquals(10, result.page);
        verify(externalRestaurantClient).fetchFoodOutletsPage(10);
    }

    private Restaurant createRestaurant(String city, String name, int cost, double rating) {
        Restaurant restaurant = new Restaurant();
        restaurant.city = city;
        restaurant.name = name;
        restaurant.estimatedCost = cost;

        Restaurant.UserRating userRating = new Restaurant.UserRating();
        userRating.averageRating = rating;
        userRating.votes = 100;
        restaurant.userRating = userRating;

        return restaurant;
    }
}
