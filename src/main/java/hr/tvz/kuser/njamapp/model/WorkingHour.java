package hr.tvz.kuser.njamapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class WorkingHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dayOfWeek;
    private LocalTime openingTime;
    private LocalTime closingTime;
    @ManyToOne
    private Restaurant restaurant;
}
