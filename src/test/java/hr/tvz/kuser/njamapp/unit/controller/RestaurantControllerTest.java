package hr.tvz.kuser.njamapp.unit.controller;

import hr.tvz.kuser.njamapp.cmd.RestaurantCommand;
import hr.tvz.kuser.njamapp.controller.RestaurantController;
import hr.tvz.kuser.njamapp.dto.RestaurantDTO;
import hr.tvz.kuser.njamapp.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RestaurantControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).setValidator(new LocalValidatorFactoryBean()).build();
    }

    @Test
    void getAllRestaurants() throws Exception {
        when(restaurantService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(restaurantService, times(1)).findAll();
    }

    @Test
    void getRestaurantById_success() throws Exception {
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, "name", "address", true, 2.0, List.of());
        when(restaurantService.findById(1L)).thenReturn(Optional.of(restaurantDTO));

        mockMvc.perform(get("/api/v1/restaurants/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.isOpen").value(true))
                .andExpect(jsonPath("$.load").value(2.0))
                .andExpect(jsonPath("$.workingHours").isArray());

        verify(restaurantService).findById(1L);
    }

    @Test
    void getRestaurantById_notFound() throws Exception {
        when(restaurantService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/restaurants/{id}", 1))
                .andExpect(status().isNotFound());

        verify(restaurantService).findById(1L);
    }


    @Test
    void getBestRatedRestaurant_success() throws Exception {
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, "name", "address", true, 2.0, List.of());
        when(restaurantService.findBestRated()).thenReturn(Optional.of(restaurantDTO));

        mockMvc.perform(get("/api/v1/restaurants/best-rated"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.isOpen").value(true))
                .andExpect(jsonPath("$.load").value(2.0))
                .andExpect(jsonPath("$.workingHours").isArray());

        verify(restaurantService).findBestRated();
    }

    @Test
    void getBestRatedRestaurant_notFound() throws Exception {
        when(restaurantService.findBestRated()).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/restaurants/best-rated"))
                .andExpect(status().isNotFound());

        verify(restaurantService).findBestRated();
    }

    @Test
    void getNearestRestaurant_success() throws Exception {
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, "name", "address", true, 2.0, List.of());
        when(restaurantService.findNearest("address")).thenReturn(Optional.of(restaurantDTO));

        mockMvc.perform(get("/api/v1/restaurants/nearest").param("address", "address"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.isOpen").value(true))
                .andExpect(jsonPath("$.load").value(2.0))
                .andExpect(jsonPath("$.workingHours").isArray());

        verify(restaurantService).findNearest("address");
    }

    @Test
    void getNearestRestaurant_notFound() throws Exception {
        when(restaurantService.findNearest("address")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/restaurants/nearest").param("address", "address"))
                .andExpect(status().isNotFound());

        verify(restaurantService).findNearest("address");
    }

    @Test
    void getRestaurantByName_success() throws Exception {
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, "name", "address", true, 2.0, List.of());
        when(restaurantService.findByName("name")).thenReturn(Optional.of(restaurantDTO));

        mockMvc.perform(get("/api/v1/restaurants/by-name").param("name", "name"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.isOpen").value(true))
                .andExpect(jsonPath("$.load").value(2.0))
                .andExpect(jsonPath("$.workingHours").isArray());

        verify(restaurantService).findByName("name");
    }

    @Test
    void getRestaurantByName_notFound() throws Exception {
        when(restaurantService.findByName("name")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/restaurants/by-name").param("name", "name"))
                .andExpect(status().isNotFound());

        verify(restaurantService).findByName("name");
    }

    @Test
    void createRestaurant_success() throws Exception {
        RestaurantCommand restaurantCommand = new RestaurantCommand(1L, "Test Restaurant", "123 Main St",
                "123-456-7890", "test@example.com", List.of(),
                true, 4, 30, 50,
                2, "A fine dining experience.");

        when(restaurantService.save(restaurantCommand)).thenReturn(true);

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
        {
            "id": 1,
            "name": "Test Restaurant",
            "address": "123 Main St",
            "phoneNumber": "123-456-7890",
            "email": "test@example.com",
            "workingHours": [],
            "isOpen": true,
            "rating": 4,
            "deliveryTime": 30,
            "maxOrders": 50,
            "michelinStars": 2,
            "description": "A fine dining experience."
        }
        """))
                .andExpect(status().isCreated());

        verify(restaurantService).save(restaurantCommand);
    }

    @Test
    void createRestaurant_conflict() throws Exception {
        RestaurantCommand restaurantCommand = new RestaurantCommand(1L, "Test Restaurant", "123 Main St",
                "123-456-7890", "test@example.com", List.of(),
                true, 4, 30, 50,
                2, "A fine dining experience.");
        when(restaurantService.save(restaurantCommand)).thenReturn(false);

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
        {
            "id": 1,
            "name": "Test Restaurant",
            "address": "123 Main St",
            "phoneNumber": "123-456-7890",
            "email": "test@example.com",
            "workingHours": [],
            "isOpen": true,
            "rating": 4,
            "deliveryTime": 30,
            "maxOrders": 50,
            "michelinStars": 2,
            "description": "A fine dining experience."
        }
        """))
                .andExpect(status().isConflict());
        verify(restaurantService).save(restaurantCommand);
    }

    @Test
    void createRestaurant_badRequest() throws Exception {
        String invalidJson = """
            {
                "id": 1,
                "name": "",
                "address": "",
                "phoneNumber": "",
        """;

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(restaurantService, never()).save(any());
    }
}