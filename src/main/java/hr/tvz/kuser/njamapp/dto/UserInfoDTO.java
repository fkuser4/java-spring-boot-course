package hr.tvz.kuser.njamapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private Set<String> roles;
}
