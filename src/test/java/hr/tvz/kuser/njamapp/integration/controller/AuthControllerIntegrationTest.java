package hr.tvz.kuser.njamapp.integration.controller;


import hr.tvz.kuser.njamapp.model.RefreshToken;
import hr.tvz.kuser.njamapp.model.UserInfo;
import hr.tvz.kuser.njamapp.service.JwtService;
import hr.tvz.kuser.njamapp.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerIntegrationTest {

    @MockitoBean
    JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        RefreshToken fakeRefreshToken = new RefreshToken();
        fakeRefreshToken.setToken("refreshToken123");

        UserInfo fakeUserInfo = new UserInfo();
        fakeUserInfo.setUsername("user");
        fakeRefreshToken.setUserInfo(fakeUserInfo);

        when(jwtService.validateToken(eq("token"), any())).thenReturn(true);
        when(jwtService.extractUsername(eq("token"))).thenReturn("user");
        when(jwtService.generateToken(eq("user"))).thenReturn("accessToken");

        when(refreshTokenService.createRefreshToken(eq("user")))
                .thenReturn(fakeRefreshToken);

        when(refreshTokenService.findByToken(eq("refreshToken123")))
                .thenReturn(Optional.of(fakeRefreshToken));

        when(refreshTokenService.verifyExpiration(any()))
                .thenReturn(fakeRefreshToken);

    }

    @Test
    public void logout_success() throws Exception {
        mockMvc.perform(
                        post("/api/v1/auth/logout")
                                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    public void logout_unauthorized() throws Exception {
        mockMvc.perform(
                        post("/api/v1/auth/logout"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void authenticateAndGetToken_success() throws Exception {
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\":\"user\", \"password\":\"user\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void authenticateAndGetToken_failure() throws Exception {
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\":\"invalidUser\", \"password\":\"invalidPassword\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void refreshToken_success() throws Exception {
        String refreshToken = "refreshToken123";

        mockMvc.perform(
                        post("/api/v1/auth/refreshToken")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"token\":\"" + refreshToken + "\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.token").value(refreshToken));
    }

    @Test
    public void refreshToken_failure() {
        String invalidRefreshToken = "invalid";

        assertThrows(
                Exception.class,
                () -> mockMvc.perform(
                        post("/api/v1/auth/refreshToken")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"token\":\"" + invalidRefreshToken + "\"}")
                ));
    }
}
