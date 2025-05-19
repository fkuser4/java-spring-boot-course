package hr.tvz.kuser.njamapp.service;

import hr.tvz.kuser.njamapp.cmd.RestaurantCommand;
import hr.tvz.kuser.njamapp.dto.RestaurantDTO;
import hr.tvz.kuser.njamapp.mapper.RestaurantMapper;
import hr.tvz.kuser.njamapp.mapper.WorkingHourMapper;
import hr.tvz.kuser.njamapp.model.Restaurant;
import hr.tvz.kuser.njamapp.model.WorkingHour;
import hr.tvz.kuser.njamapp.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Override
    public Optional<RestaurantDTO> findById(Long id) {
        return restaurantRepository.findById(id).map(RestaurantMapper::toDTO);
    }

    @Override
    public List<RestaurantDTO> findAll() {
        return restaurantRepository.findAll().stream().map(RestaurantMapper::toDTO).toList();
    }

    @Override
    public Optional<RestaurantDTO> findNearest(String address) {
        return restaurantRepository.findFirstByAddressContainsIgnoreCase(address).map(RestaurantMapper::toDTO);
    }

    @Override
    public Optional<RestaurantDTO> findBestRated() {
        return restaurantRepository.findFirstByOrderByRatingDesc().map(RestaurantMapper::toDTO);
    }

    @Override
    public Optional<RestaurantDTO> findByName(String name) {
        return restaurantRepository.findByName(name).map(RestaurantMapper::toDTO);
    }

    @Override
    public boolean deleteById(Long id) {
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean save(RestaurantCommand restaurantCommand) {
        if (restaurantRepository.existsByNameAndAddress(restaurantCommand.getName(), restaurantCommand.getAddress())) {
            return false;
        }

        restaurantCommand.setId(null);

        Restaurant restaurant = RestaurantMapper.toEntity(restaurantCommand);

        if (restaurant.getWorkingHours() != null) {
            restaurant.getWorkingHours().forEach(wh -> wh.setRestaurant(restaurant));
        }

        restaurantRepository.save(restaurant);
        return true;
    }

    @Override
    public boolean update(Long id, RestaurantCommand command) {
        Optional<Restaurant> existingOpt = restaurantRepository.findById(id);

        if (existingOpt.isEmpty()) {
            return false;
        }

        Restaurant existing = existingOpt.get();

        existing.setName(command.getName());
        existing.setAddress(command.getAddress());
        existing.setPhoneNumber(command.getPhoneNumber());
        existing.setEmail(command.getEmail());
        existing.setIsOpen(command.getIsOpen());
        existing.setRating(command.getRating());
        existing.setDeliveryTime(command.getDeliveryTime());
        existing.setMaxOrders(command.getMaxOrders());
        existing.setMichelinStars(command.getMichelinStars());
        existing.setDescription(command.getDescription());

        restaurantRepository.save(existing);
        return true;
    }

}
