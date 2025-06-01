package hr.tvz.kuser.njamapp.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.tvz.kuser.njamapp.cmd.RestaurantCommand;
import hr.tvz.kuser.njamapp.cmd.WorkingHourCommand;
import hr.tvz.kuser.njamapp.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
public class RestaurantControllerIntegrationTest {

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

        when(jwtService.validateToken(eq("tokenAdmin"), any())).thenReturn(true);
        when(jwtService.extractUsername(eq("tokenAdmin"))).thenReturn("admin");
    }

    @Test
    void testGetRestaurants_success() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("Le Gourmet"));
    }

    @Test
    void testGetRestaurants_unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetRestaurantById_success() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/{id}", 1)
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetRestaurantById_notFound() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/{id}", 999)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetRestaurantById_unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/{id}", 1))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetBestRatedRestaurant_success() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/best-rated")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Le Gourmet"));
    }

    @Test
    void testGetBestRatedRestaurant_unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/best-rated"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testNearestRestaurant_success() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/nearest")
                        .param("address", "Pariz")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Le Gourmet"));
    }

    @Test
    void testNearestRestaurant_notFound() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/nearest")
                        .param("address", "Unknown Location")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testNearestRestaurant_unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/nearest")
                        .param("address", "Pariz"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetRestaurantByName_success() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/by-name")
                        .param("name", "Le Gourmet")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Le Gourmet"));
    }

    @Test
    void testGetRestaurantByName_notFound() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/by-name")
                        .param("name", "Nonexistent Restaurant")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetRestaurantByName_unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/by-name")
                        .param("name", "Le Gourmet"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateRestaurant_success() throws Exception {
        RestaurantCommand command = new RestaurantCommand();
        command.setId(1L);
        command.setName("Test Restaurant");
        command.setAddress("123 Test St");
        command.setPhoneNumber("+38512345678");
        command.setEmail("test@restaurant.com");
        command.setIsOpen(true);
        command.setRating(4);
        command.setDeliveryTime(20);
        command.setMaxOrders(10);
        command.setMichelinStars(2);
        command.setDescription("Nice test place");
        List<WorkingHourCommand> workingHours = List.of(
                new WorkingHourCommand() {{
                    setDayOfWeek("Monday");
                    setOpeningTime(LocalTime.parse("09:00"));
                    setClosingTime(LocalTime.parse("17:00"));
                }},
                new WorkingHourCommand() {{
                    setDayOfWeek("Tuesday");
                    setOpeningTime(LocalTime.parse("09:00"));
                    setClosingTime(LocalTime.parse("17:00"));
                }}
        );
        command.setWorkingHours(workingHours);

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .header("Authorization", "Bearer tokenAdmin"))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateRestaurant_conflict() throws Exception {
        RestaurantCommand command = new RestaurantCommand();
        command.setId(1L);
        command.setName("Le Gourmet");
        command.setAddress("Ulica Pariz 123");
        command.setPhoneNumber("+33 1 23 45 67 89");
        command.setEmail("kontakt@legourmet.fr");
        command.setIsOpen(true);
        command.setRating(5);
        command.setDeliveryTime(30);
        command.setMaxOrders(50);
        command.setMichelinStars(5);
        command.setDescription("Fine dining with French cuisine.");
        List<WorkingHourCommand> workingHours = List.of(
                new WorkingHourCommand() {{
                    setDayOfWeek("MONDAY");
                    setOpeningTime(LocalTime.parse("09:00"));
                    setClosingTime(LocalTime.parse("17:00"));
                }},
                new WorkingHourCommand() {{
                    setDayOfWeek("TUESDAY");
                    setOpeningTime(LocalTime.parse("09:00"));
                    setClosingTime(LocalTime.parse("17:00"));
                }}
        );
        command.setWorkingHours(workingHours);

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .header("Authorization", "Bearer tokenAdmin"))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateRestaurant_badRequest() throws Exception {
        RestaurantCommand command = new RestaurantCommand();
        command.setName("");

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .header("Authorization", "Bearer tokenAdmin"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateRestaurant_unauthorized() throws Exception {
        RestaurantCommand command = new RestaurantCommand();
        command.setName("Unauthorized Restaurant");

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateRestaurant_forbidden() throws Exception {
        RestaurantCommand command = new RestaurantCommand();
        command.setId(1L);
        command.setName("Test Restaurant");
        command.setAddress("123 Test St");
        command.setPhoneNumber("+38512345678");
        command.setEmail("test@restaurant.com");
        command.setIsOpen(true);
        command.setRating(4);
        command.setDeliveryTime(20);
        command.setMaxOrders(10);
        command.setMichelinStars(2);
        command.setDescription("Nice test place");
        List<WorkingHourCommand> workingHours = List.of(
                new WorkingHourCommand() {{
                    setDayOfWeek("Monday");
                    setOpeningTime(LocalTime.parse("09:00"));
                    setClosingTime(LocalTime.parse("17:00"));
                }},
                new WorkingHourCommand() {{
                    setDayOfWeek("Tuesday");
                    setOpeningTime(LocalTime.parse("09:00"));
                    setClosingTime(LocalTime.parse("17:00"));
                }}
        );
        command.setWorkingHours(workingHours);

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isForbidden());
    }
}
