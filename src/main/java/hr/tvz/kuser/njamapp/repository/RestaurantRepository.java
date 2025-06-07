package hr.tvz.kuser.njamapp.repository;

import hr.tvz.kuser.njamapp.model.Restaurant;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findFirstByAddressContainsIgnoreCase(String address);

    Optional<Restaurant> findFirstByOrderByRatingDesc();

    Optional<Restaurant> findByName(String name);

    boolean existsByNameAndAddress(@NotBlank(message = "Name is required") String name,
            @NotBlank(message = "Address is required") String address);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.workingHours WHERE r.isOpen = true")
    List<Restaurant> findByIsOpenTrue();
}
