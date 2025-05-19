package hr.tvz.kuser.njamapp.controller;

import hr.tvz.kuser.njamapp.cmd.ReviewCommand;
import hr.tvz.kuser.njamapp.dto.ReviewDTO;
import hr.tvz.kuser.njamapp.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/restaurants/{restaurantId}/reviews")
public class RestaurantReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public List<ReviewDTO> getReviews(@PathVariable Long restaurantId) {
        return reviewService.findAllByRestaurantId(restaurantId);
    }

    @PostMapping
    public ResponseEntity<Void> addReview(@PathVariable Long restaurantId, @Valid @RequestBody ReviewCommand command) {
        boolean created = reviewService.save(restaurantId, command);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable Long restaurantId, @PathVariable Long reviewId, @Valid @RequestBody ReviewCommand command) {
        boolean updated = reviewService.save(restaurantId, reviewId, command);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId, @PathVariable Long reviewId) {
        if (reviewService.deleteById(reviewId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
