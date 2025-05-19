package hr.tvz.kuser.njamapp.controller;

import hr.tvz.kuser.njamapp.dto.UserInfoDTO;
import hr.tvz.kuser.njamapp.service.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserInfoService userService;

    @GetMapping("/current-user")
    public ResponseEntity<UserInfoDTO> getCurrentUser() {
        return userService.getCurrentUser()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
