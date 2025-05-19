package hr.tvz.kuser.njamapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String text;
    private Integer rating;
    @ManyToOne
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo user;
}
