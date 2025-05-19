package hr.tvz.kuser.njamapp.cmd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class WorkingHourCommand {

    @NotBlank(message = "Day of week is required")
    private String dayOfWeek;

    @NotNull(message = "Opening time is required")
    private LocalTime openingTime;

    @NotNull(message = "Closing time is required")
    private LocalTime closingTime;
}
