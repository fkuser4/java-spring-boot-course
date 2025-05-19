package hr.tvz.kuser.njamapp.mapper;

import hr.tvz.kuser.njamapp.cmd.WorkingHourCommand;
import hr.tvz.kuser.njamapp.dto.WorkingHourDTO;
import hr.tvz.kuser.njamapp.model.WorkingHour;

public class WorkingHourMapper {
    public static WorkingHour toEntity(WorkingHourCommand command) {
        WorkingHour entity = new WorkingHour();
        entity.setDayOfWeek(command.getDayOfWeek());
        entity.setOpeningTime(command.getOpeningTime());
        entity.setClosingTime(command.getClosingTime());
        return entity;
    }

    public static WorkingHourDTO toDTO(WorkingHour workingHour) {
        return new WorkingHourDTO(workingHour.getDayOfWeek(), workingHour.getOpeningTime(),
                workingHour.getClosingTime());
    }
}
