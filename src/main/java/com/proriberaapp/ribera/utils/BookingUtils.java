package com.proriberaapp.ribera.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

public class BookingUtils {


    public static String calculateBookingType(LocalDate startDate, LocalDate endDate) {
        Set<DayOfWeek> weekendDays = EnumSet.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
        Set<DayOfWeek> regularDays = EnumSet.of(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY);

        boolean hasRegular = false;
        boolean hasWeekend = false;

        LocalDate date = startDate;
        while (!date.isAfter(endDate.minusDays(1))) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            if (regularDays.contains(dayOfWeek)) {
                hasRegular = true;
            }
            if (weekendDays.contains(dayOfWeek)) {
                hasWeekend = true;
            }

            date = date.plusDays(1);
        }

        if (hasRegular && !hasWeekend) {
            return "DIAS_REGULARES";
        } else if (!hasRegular && hasWeekend) {
            return "FINES_DE_SEMANA";
        } else {
            return "DIAS_REGULARES";
        }
    }

}
