package hr.tvz.kuser.njamapp.service;

import hr.tvz.kuser.njamapp.cmd.ReviewCommand;
import hr.tvz.kuser.njamapp.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> findAllByRestaurantId(Long restaurantId);
    boolean save(Long restaurantId, ReviewCommand cmd);
    boolean save(Long restaurantId, Long reviewId, ReviewCommand cmd);
    boolean deleteById(Long reviewId);
}
