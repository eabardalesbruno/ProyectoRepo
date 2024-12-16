package com.proriberaapp.ribera.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class GeneralMethods {
    public static String generatePassword(String firstName, String lastName) {
        String cleanedFirstName = firstName.replaceAll("\\s+", "");
        String cleanedLastName = lastName.replaceAll("\\s+", "");
        String firstPart = cleanedFirstName.length() > 2 ? cleanedFirstName.substring(0, 3) : cleanedFirstName;
        String lastPart = cleanedLastName.length() > 2 ? cleanedLastName.substring(0, 3) : cleanedLastName;
        Random random = new Random();
        int randomNumber = random.nextInt(900) + 100;
        return firstPart + lastPart + randomNumber;
    }

    public static boolean esNombreDeMes(String nombre) {
        return List.of("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
                .contains(nombre);
    }

    public static BigDecimal calculateCostTotal(float adultCost, float adultExtraCost, float adultMayorCost,
            float kidCost, float infantCost, int numberAdult,
            int numberAdultExtra,
            int numberAdultMayor,
            int numberBaby,
            int numberChild, int numberDays) {
        return new BigDecimal(adultCost * numberAdult + adultExtraCost * numberAdultExtra
                + adultMayorCost * numberAdultMayor + kidCost * numberChild + infantCost * numberBaby)
                .multiply(new BigDecimal(numberDays));
    }
}
