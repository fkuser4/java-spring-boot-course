package hr.tvz.kuser.njamapp.mapper;

import hr.tvz.kuser.njamapp.cmd.ReviewCommand;
import hr.tvz.kuser.njamapp.dto.ReviewDTO;
import hr.tvz.kuser.njamapp.model.Review;

public class ReviewMapper {
    public static ReviewDTO toDTO(Review review) {
        return new ReviewDTO(review.getId(), review.getTitle(), review.getText(), review.getRating(), review.getUser().getUsername());
    }

    public static Review toEntity(ReviewCommand cmd) {
        Review r = new Review();
        r.setTitle(cmd.getTitle());
        r.setText(cmd.getText());
        r.setRating(cmd.getRating());
        return r;
    }
}
