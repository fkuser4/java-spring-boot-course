package hr.tvz.kuser.njamapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Data
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_authority", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<UserRole> roles;
}
