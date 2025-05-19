package hr.tvz.kuser.njamapp.cmd;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewCommand {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be at most 100 characters")
    private String title;

    @NotBlank(message = "Review text is required")
    @Size(max = 1000, message = "Review text must be at most 1000 characters")
    private String text;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
}
