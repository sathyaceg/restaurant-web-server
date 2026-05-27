package com.example.demo;

import com.example.demo.model.Restaurant;
import com.example.demo.model.RestaurantResponse;
import com.example.demo.service.RestaurantService;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<Void> addRestaurant(@RequestBody String rawJson) {
        log.info("Adding restaurant with body: {}", rawJson);
        Gson gson = new Gson();
        Restaurant restaurant = gson.fromJson(rawJson, Restaurant.class);
        validateRestaurant(restaurant);
        restaurantService.addRestaurant(restaurant);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/top")
    public ResponseEntity<Restaurant> getTopRestaurant(@RequestParam(required = false) String city,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) Integer budget) {
        Double budgetValue = budget == null ? null : Double.valueOf(budget);
        Restaurant restaurant = restaurantService.getRestaurantByFilter(city, budgetValue);
        if (restaurant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(restaurant);
    }

    @GetMapping("/external")
    public ResponseEntity<RestaurantResponse> getExternalRestaurants(@RequestParam(defaultValue = "10") int page) {
        return ResponseEntity.ok(restaurantService.fetchFoodOutletsPage(page));
    }

    private void validateRestaurant(Restaurant restaurant) {
        Preconditions.checkArgument(restaurant.name != null, "Restaurant name must not be null");
        Preconditions.checkArgument(restaurant.city != null, "Restaurant city must not be null");
    }
}
