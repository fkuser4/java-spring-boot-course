package hr.tvz.kuser.njamapp.service;

import hr.tvz.kuser.njamapp.cmd.ReviewCommand;
import hr.tvz.kuser.njamapp.dto.ReviewDTO;
import hr.tvz.kuser.njamapp.mapper.ReviewMapper;
import hr.tvz.kuser.njamapp.model.Restaurant;
import hr.tvz.kuser.njamapp.model.Review;
import hr.tvz.kuser.njamapp.model.UserInfo;
import hr.tvz.kuser.njamapp.repository.RestaurantRepository;
import hr.tvz.kuser.njamapp.repository.ReviewRepository;
import hr.tvz.kuser.njamapp.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;
    UserInfoRepository userInfoRepository;
    RestaurantRepository restaurantRepository;

    @Override
    public List<ReviewDTO> findAllByRestaurantId(Long restaurantId) {
        return reviewRepository.findAllByRestaurantId(restaurantId).stream().map(ReviewMapper::toDTO).toList();
    }

    @Override
    public boolean save(Long restaurantId, ReviewCommand cmd) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo = userInfoRepository.findByUsername(username);

        if (reviewRepository.existsByRestaurantIdAndUserId(restaurantId, userInfo.getId())) {
            return false;
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

        Review review = ReviewMapper.toEntity(cmd);
        review.setRestaurant(restaurant);
        review.setUser(userInfo);

        reviewRepository.save(review);
        return true;
    }

    @Override
    public boolean save(Long restaurantId, Long reviewId, ReviewCommand cmd) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        if (review.isEmpty()) {
            return false;
        }

        if (!review.get().getRestaurant().getId().equals(restaurantId)) {
            return false;
        }

        Review reviewUnpacked = review.get();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !reviewUnpacked.getUser().getUsername().equals(username)) {
            return false;
        }

        reviewUnpacked.setTitle(cmd.getTitle());
        reviewUnpacked.setText(cmd.getText());
        reviewUnpacked.setRating(cmd.getRating());

        reviewRepository.save(reviewUnpacked);
        return true;
    }

    @Override
    public boolean deleteById(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            return false;
        }
        reviewRepository.deleteById(reviewId);
        return true;
    }


}
