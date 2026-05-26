package com.example.demo;

import com.example.demo.data.RestaurantDataStore;
import com.example.demo.model.Restaurant;
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
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/top")
    public ResponseEntity<Void> getTopRestaurant() {
        // TODO: add query params (city, budget) and selection logic
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    private void validateRestaurant(Restaurant restaurant) {
        Preconditions.checkArgument(restaurant.name!= null, "Restaurant name must not be null");
        Preconditions.checkArgument(restaurant.city!= null, "Restaurant city must not be null");
    }
}
