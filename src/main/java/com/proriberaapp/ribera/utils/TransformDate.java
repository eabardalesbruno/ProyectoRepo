package com.proriberaapp.ribera.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class TransformDate {
    public static String getMonthDayOfWeekAndNumber(Timestamp timestamp, String pattern) {
        // Formateador para el mes, día de la semana abreviados y número de día
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, new Locale("es", "ES"));

        // Convertir el Timestamp a Date y formatearlo
        return formatter.format(timestamp);
    }

    public static String getMonthDayOfWeekAndNumber(Timestamp timestamp) {
        // Formateador para el mes, día de la semana abreviados y número de día
        SimpleDateFormat formatter = new SimpleDateFormat("MMM EEE d", new Locale("es", "ES"));

        // Convertir el Timestamp a Date y formatearlo
        return formatter.format(timestamp);
    }

    public static String getAbbreviatedMonth(Timestamp timestamp) {
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MMM", new Locale("es", "ES"));
        return monthFormatter.format(timestamp);
    }

    public static String calculatePersons(int numberAdults, int numberChildren, int numberBabies, int numberAdultsExtra,
            int numberAdultsMayor) {
        StringBuilder result = new StringBuilder();

        if (numberAdults > 0) {
            result.append(numberAdults).append(" adulto").append(numberAdults > 1 ? "s" : "").append(" ");
        }
        if (numberChildren > 0) {
            result.append(numberChildren).append(" niño").append(numberChildren > 1 ? "s" : "").append(" ");
        }
        if (numberBabies > 0) {
            result.append(numberBabies).append(" bebé").append(numberBabies > 1 ? "s" : "").append(" ");
        }
        if (numberAdultsExtra > 0) {
            result.append(numberAdultsExtra).append(" adulto extra")
                    .append(numberAdultsExtra > 1 ? "s" : "").append(" ");
        }
        if (numberAdultsMayor > 0) {
            result.append(numberAdultsMayor).append(" adulto mayor")
                    .append(numberAdultsMayor > 1 ? "es" : "").append(" ");
        }

        // Elimina espacios extra al final
        return result.toString().trim();
    }

    public static String calculatePersonsAdultAndKids(int numberAdults, int numberChildren, int numberBabies,
            int numberAdultsExtra,
            int numberAdultsMayor) {
        StringBuilder result = new StringBuilder();
        String prefixAdult = "Adulto";
        String prefixChild = "Niño";
        int numberTotalAdults = numberAdults + numberAdultsExtra + numberAdultsMayor;
        int numberTotalChildren = numberChildren + numberBabies;
        if (numberTotalAdults > 1) {
            prefixAdult = "Adultos";
        }
        if (numberTotalChildren > 1) {
            prefixChild = "Niños";
        }
        result.append(numberTotalAdults).append(" ").append(prefixAdult).append(" ").append(numberTotalChildren)
                .append(" ").append(prefixChild);

        return result.toString().trim();
    }

    // Función para obtener el día de la semana abreviado
    public static String getAbbreviatedDayOfWeek(Timestamp timestamp) {
        SimpleDateFormat dayFormatter = new SimpleDateFormat("EEE", new Locale("es", "ES"));
        return dayFormatter.format(timestamp);
    }

    // Función para obtener el número del día
    public static int getDayNumber(Timestamp timestamp) {
        SimpleDateFormat dayNumberFormatter = new SimpleDateFormat("d", new Locale("es", "ES"));
        return Integer.parseInt(dayNumberFormatter.format(timestamp));
    }

    public static long calculateDaysDifference(LocalDate dayBookingInit, LocalDate dayBookingEnd) {
        if (dayBookingInit == null || dayBookingEnd == null) {
            throw new IllegalArgumentException("Both Timestamps must be non-null");
        }

     

        // Calcular la diferencia de días
        return ChronoUnit.DAYS.between(dayBookingInit, dayBookingEnd);
    }
    public static long calculateDaysDifference(Timestamp dayBookingInit, Timestamp dayBookingEnd) {
        if (dayBookingInit == null || dayBookingEnd == null) {
            throw new IllegalArgumentException("Both Timestamps must be non-null");
        }

        // Convertir los Timestamp a LocalDate
        LocalDate startDate = dayBookingInit.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate endDate = dayBookingEnd.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Calcular la diferencia de días
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

}
