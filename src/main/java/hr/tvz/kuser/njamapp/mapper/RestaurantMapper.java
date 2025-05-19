package hr.tvz.kuser.njamapp.mapper;

import hr.tvz.kuser.njamapp.cmd.RestaurantCommand;
import hr.tvz.kuser.njamapp.dto.RestaurantDTO;
import hr.tvz.kuser.njamapp.model.Restaurant;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class RestaurantMapper {
    public static RestaurantDTO toDTO(Restaurant restaurant) {

        int activeOrders = ThreadLocalRandom.current().nextInt(10, 101);
        int maxOrders = restaurant.getMaxOrders();

        double load = maxOrders > 0 ? Math.min(100.0, (activeOrders * 100.0) / maxOrders) : 0.0;

        return new RestaurantDTO(restaurant.getId(), restaurant.getName(), restaurant.getAddress(),
                restaurant.getIsOpen(), load,
                restaurant.getWorkingHours().stream().map(WorkingHourMapper::toDTO).toList());
    }

    public static Restaurant toEntity(RestaurantCommand restaurantCommand) {
        return new Restaurant(restaurantCommand.getId(), restaurantCommand.getName(), restaurantCommand.getAddress(),
                restaurantCommand.getPhoneNumber(), restaurantCommand.getEmail(),
                restaurantCommand.getWorkingHours().stream().map(WorkingHourMapper::toEntity).toList(),
                restaurantCommand.getIsOpen(), restaurantCommand.getRating(), restaurantCommand.getDeliveryTime(),
                restaurantCommand.getMaxOrders(), restaurantCommand.getMichelinStars(),
                restaurantCommand.getDescription(), new ArrayList<>());
    }
}
