package hr.tvz.kuser.njamapp.repository;

import hr.tvz.kuser.njamapp.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByRestaurantId(Long restaurantId);
    boolean existsByRestaurantIdAndUserId(Long restaurantId, Long userId);
}
