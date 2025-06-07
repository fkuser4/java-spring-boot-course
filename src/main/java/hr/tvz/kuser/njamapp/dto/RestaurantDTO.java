package hr.tvz.kuser.njamapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
@AllArgsConstructor
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private Boolean isOpen;
    private Double load;
    private List<WorkingHourDTO> workingHours;
}
