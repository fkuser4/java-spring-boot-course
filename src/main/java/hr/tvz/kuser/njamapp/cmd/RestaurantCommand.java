package hr.tvz.kuser.njamapp.cmd;

import jakarta.validation.Valid;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantCommand {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Working hours are required")
    @Valid
    private List<WorkingHourCommand> workingHours;
    @NotNull(message = "Open status must be provided")
    private Boolean isOpen;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Min(value = 1, message = "Delivery time must be positive")
    private Integer deliveryTime;

    @Min(value = 1, message = "Max orders must be positive")
    private Integer maxOrders;

    @Min(value = 1, message = "Michelin stars must be positive")
    @Max(value = 5, message = "Michelin stars must be at most 5")
    private Integer michelinStars;

    @NotBlank(message = "Description is required")
    private String description;
}
