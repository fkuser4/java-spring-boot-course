package hr.tvz.kuser.njamapp.job;

import hr.tvz.kuser.njamapp.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@AllArgsConstructor
public class JobStore extends QuartzJobBean {

    private RestaurantService restaurantService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        restaurantService.findCurrentlyOpenRestaurants().forEach(System.out::println);
    }
}
