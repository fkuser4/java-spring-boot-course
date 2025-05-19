package hr.tvz.kuser.njamapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkingHour> workingHours;
    private Boolean isOpen;
    private Integer rating;
    private Integer deliveryTime;
    private Integer maxOrders;
    private Integer michelinStars;
    private String description;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews;
}
