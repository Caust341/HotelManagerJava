package util.validator;

import model.entity.BookingEntity;
import service.BookingService;

import java.time.LocalDate;

public class BookingValidator {
    public static ValidationResult IsCheckInValid(LocalDate checkin) {
        if(checkin.isBefore(LocalDate.now())) {
            return ValidationResult.fail("Checkin needs to be after or on the current date");
        }
        return ValidationResult.success();
    }

}
