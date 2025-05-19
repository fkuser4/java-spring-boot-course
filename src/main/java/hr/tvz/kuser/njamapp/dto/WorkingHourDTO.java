package hr.tvz.kuser.njamapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class WorkingHourDTO {
    private String dayOfWeek;
    private LocalTime openingTime;
    private LocalTime closingTime;
}
