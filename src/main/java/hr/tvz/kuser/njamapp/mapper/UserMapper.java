package hr.tvz.kuser.njamapp.mapper;

import hr.tvz.kuser.njamapp.dto.UserInfoDTO;
import hr.tvz.kuser.njamapp.model.UserInfo;
import hr.tvz.kuser.njamapp.model.UserRole;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserInfoDTO toDTO(UserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }

        return UserInfoDTO.builder()
                .id(userInfo.getId())
                .username(userInfo.getUsername())
                .name(userInfo.getName())
                .surname(userInfo.getSurname())
                .roles(userInfo.getRoles().stream()
                        .map(UserRole::getRole)
                        .collect(Collectors.toSet()))
                .build();
    }
}
