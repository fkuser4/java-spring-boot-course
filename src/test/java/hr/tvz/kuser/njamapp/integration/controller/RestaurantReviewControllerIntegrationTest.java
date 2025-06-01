package hr.tvz.kuser.njamapp.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.tvz.kuser.njamapp.cmd.ReviewCommand;
import hr.tvz.kuser.njamapp.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
public class RestaurantReviewControllerIntegrationTest {

    @MockitoBean
    JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        when(jwtService.validateToken(eq("token"), any())).thenReturn(true);
        when(jwtService.extractUsername(eq("token"))).thenReturn("user");
    }

    @Test
    void testGetRestaurantReviews_success() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/{restaurantId}/reviews", 1)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Amazing Experience"));
    }

    @Test
    void testGetRestaurantReviews_unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/{restaurantId}/reviews", 1))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAddReview_success() throws Exception {
        ReviewCommand command = new ReviewCommand("title", "text", 4);

        mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/reviews", 3)
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddReview_conflict() throws Exception {
        ReviewCommand command = new ReviewCommand("title", "text", 4);

        mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/reviews", 1)
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isConflict());
    }

    @Test
    void testAddReview_badRequest() throws Exception {
        ReviewCommand command = new ReviewCommand("", "text", 4);

        mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/reviews", 3)
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());
    }
}
