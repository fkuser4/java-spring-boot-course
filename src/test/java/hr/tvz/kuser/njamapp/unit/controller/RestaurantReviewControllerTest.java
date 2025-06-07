package hr.tvz.kuser.njamapp.unit.controller;

import hr.tvz.kuser.njamapp.cmd.ReviewCommand;
import hr.tvz.kuser.njamapp.controller.RestaurantReviewController;
import hr.tvz.kuser.njamapp.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import java.util.List;

import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantReviewControllerTest {

    @InjectMocks
    private RestaurantReviewController restaurantReviewController;

    @Mock
    private ReviewService reviewService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantReviewController).setValidator(new LocalValidatorFactoryBean()).build();
    }

    @Test
    void getReviews() throws Exception {
        when(reviewService.findAllByRestaurantId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/restaurants/{restaurantId}/reviews", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void addReview_created() throws Exception {
        ReviewCommand command = new ReviewCommand("title", "text", 4);
        when(reviewService.save(1L, command)).thenReturn(true);

        mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/reviews", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" :  \"title\", \"text\" : \"text\", \"rating\" :  \"4\"}")).andExpect(status().isCreated());

        verify(reviewService).save(1L, command);
    }

    @Test
    void addReview_conflict() throws Exception {
        ReviewCommand command = new ReviewCommand("title", "text", 4);
        when(reviewService.save(1L, command)).thenReturn(false);

        mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/reviews", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" :  \"title\", \"text\" : \"text\", \"rating\" :  \"4\"}")).andExpect(status().isConflict());

        verify(reviewService).save(1L, command);
    }

    @Test
    void addReview_badRequest() throws Exception {
        String invalidJson = "{\"title\": \"\", \"text\": \"\", \"rating\": 0}";

        mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/reviews", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(reviewService, never()).save(any(), any());
    }

    @Test
    void deleteReview_success() throws Exception {
        when(reviewService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/restaurants/{restaurantId}/reviews/{reviewId}", 1L, 1L))
                .andExpect(status().isNoContent());

        verify(reviewService).deleteById(1L);
    }

    @Test
    void deleteReview_notFound() throws Exception {
        when(reviewService.deleteById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/restaurants/{restaurantId}/reviews/{reviewId}", 1L, 1L))
                .andExpect(status().isNotFound());

        verify(reviewService).deleteById(1L);
    }
}