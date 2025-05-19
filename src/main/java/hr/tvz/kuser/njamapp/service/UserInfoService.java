package hr.tvz.kuser.njamapp.service;

import hr.tvz.kuser.njamapp.dto.UserInfoDTO;
import hr.tvz.kuser.njamapp.mapper.UserMapper;
import hr.tvz.kuser.njamapp.model.UserInfo;
import hr.tvz.kuser.njamapp.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserMapper userMapper;

    public Optional<UserInfoDTO> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo = userInfoRepository.findByUsername(username);
        return Optional.ofNullable(userMapper.toDTO(userInfo));
    }
}

