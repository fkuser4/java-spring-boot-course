package hr.tvz.kuser.njamapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String role;
}
