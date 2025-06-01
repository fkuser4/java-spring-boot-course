package hr.tvz.kuser.njamapp.unit.controller;

import hr.tvz.kuser.njamapp.controller.UserController;
import hr.tvz.kuser.njamapp.dto.UserInfoDTO;
import hr.tvz.kuser.njamapp.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {
    @Mock
    private UserInfoService userInfoService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getCurrentUser_success() throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO(1L, "testuser", "name", "surname", Set.of("ROLE_USER"));
        when(userInfoService.getCurrentUser()).thenReturn(Optional.of(userInfoDTO));

        mockMvc.perform(get("/api/v1/users/current-user")
                .header("Authorization", "Bearer refreshToken123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.surname").value("surname"))
                .andExpect(jsonPath("$.roles").isArray());

        verify(userInfoService).getCurrentUser();
    }

    @Test
    void getCurrentUser_failure() throws Exception {
        when(userInfoService.getCurrentUser()).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/users/current-user")
                .header("Authorization", "Bearer refreshToken123"))
                .andExpect(status().isNotFound());
    }
}