package hr.tvz.kuser.njamapp.controller;

import hr.tvz.kuser.njamapp.cmd.RestaurantCommand;
import hr.tvz.kuser.njamapp.cmd.ReviewCommand;
import hr.tvz.kuser.njamapp.dto.RestaurantDTO;
import hr.tvz.kuser.njamapp.dto.ReviewDTO;
import hr.tvz.kuser.njamapp.service.RestaurantService;
import hr.tvz.kuser.njamapp.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/restaurants")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long id) {
        return restaurantService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/best-rated")
    public ResponseEntity<RestaurantDTO> getBestRatedRestaurant() {
        return restaurantService.findBestRated().map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nearest")
    public ResponseEntity<RestaurantDTO> getNearestRestaurant(@RequestParam String address) {
        return restaurantService.findNearest(address).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-name")
    public ResponseEntity<RestaurantDTO> getRestaurantByName(@RequestParam String name) {
        return restaurantService.findByName(name).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createRestaurant(@Valid @RequestBody RestaurantCommand command) {
        boolean created = restaurantService.save(command);

        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable Long restaurantId, @Valid @RequestBody RestaurantCommand command) {
        boolean updated = restaurantService.update(restaurantId, command);

        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId) {
        if (restaurantService.deleteById(restaurantId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
