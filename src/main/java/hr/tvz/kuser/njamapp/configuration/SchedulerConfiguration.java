package hr.tvz.kuser.njamapp.configuration;

import hr.tvz.kuser.njamapp.job.JobStore;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfiguration {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(JobStore.class).withIdentity("openRestaurantsJob").storeDurably().build();
    }

    @Bean
    public Trigger trigger() {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever();

        return TriggerBuilder.newTrigger().forJob(jobDetail()).withIdentity("openRestaurantsTrigger").withSchedule(simpleScheduleBuilder).build();
    }
}
