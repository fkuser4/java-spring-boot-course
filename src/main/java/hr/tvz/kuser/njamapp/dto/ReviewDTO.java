package hr.tvz.kuser.njamapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private String title;
    private String text;
    private Integer rating;
    private String authorUsername;
}
