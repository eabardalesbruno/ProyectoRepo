package com.proriberaapp.ribera.utils;

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
}
