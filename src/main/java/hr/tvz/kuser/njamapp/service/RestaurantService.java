package hr.tvz.kuser.njamapp.service;

import hr.tvz.kuser.njamapp.cmd.RestaurantCommand;
import hr.tvz.kuser.njamapp.dto.RestaurantDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    Optional<RestaurantDTO> findById(Long id);

    List<RestaurantDTO> findAll();

    Optional<RestaurantDTO> findNearest(String address);

    Optional<RestaurantDTO> findBestRated();

    boolean deleteById(Long id);

    Optional<RestaurantDTO> findByName(String name);

    boolean save(RestaurantCommand restaurantCommand);

    boolean update(Long id, RestaurantCommand restaurantCommand);

    List<RestaurantDTO> findCurrentlyOpenRestaurants();
}
