package hr.tvz.kuser.njamapp.unit.controller;

import hr.tvz.kuser.njamapp.controller.AuthController;
import hr.tvz.kuser.njamapp.model.RefreshToken;
import hr.tvz.kuser.njamapp.model.UserInfo;
import hr.tvz.kuser.njamapp.service.JwtService;
import hr.tvz.kuser.njamapp.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void authenticateAndGetToken_success() throws Exception {
        Authentication authentication = mock(Authentication.class);
        RefreshToken refreshToken = new RefreshToken(1, "refreshToken123", Instant.now().plusSeconds(3600), null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(refreshTokenService.createRefreshToken("testuser"))
                .thenReturn(refreshToken);
        when(jwtService.generateToken("testuser"))
                .thenReturn("accessToken123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken123"))
                .andExpect(jsonPath("$.token").value("refreshToken123"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(refreshTokenService).createRefreshToken("testuser");
        verify(jwtService).generateToken("testuser");
    }

    @Test
    public void authenticateAndGetToken_failure() {
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                    .andExpect(status().isInternalServerError());
        });
    }

    @Test
    public void refreshToken_success() throws Exception {

        UserInfo userInfo = new UserInfo(1L, "testuser", "hashedPassword", "name", "surname", List.of());
        RefreshToken refreshToken = new RefreshToken(1, "refreshToken123", Instant.now().plusSeconds(3600), userInfo);

        when(refreshTokenService.findByToken("refreshToken123"))
                .thenReturn(Optional.of(refreshToken));
        when(refreshTokenService.verifyExpiration(refreshToken))
                .thenReturn(refreshToken);
        when(jwtService.generateToken("testuser"))
                .thenReturn("newAccessToken123");

        mockMvc.perform(post("/api/v1/auth/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"refreshToken123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newAccessToken123"))
                .andExpect(jsonPath("$.token").value("refreshToken123"));

        verify(refreshTokenService).findByToken("refreshToken123");
        verify(refreshTokenService).verifyExpiration(refreshToken);
        verify(jwtService).generateToken("testuser");
    }

    @Test
    public void logout_shouldDeleteRefreshToken() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        try (MockedStatic<SecurityContextHolder> mockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("testuser");

            mockMvc.perform(post("/api/v1/auth/logout"))
                    .andExpect(status().isOk());

            verify(refreshTokenService).deleteByUsername("testuser");

            mockedStatic.verify(SecurityContextHolder::clearContext);
        }
    }
}
