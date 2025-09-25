package com.proriberaapp.ribera.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.proriberaapp.ribera.Api.controllers.admin.dto.FeedingItemsGrouped;
import com.proriberaapp.ribera.Domain.entities.FeedingEntity;

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

        public static BigDecimal calculatedFeeding(float value,
                        Integer quantity) {
                return new BigDecimal(value).multiply(new BigDecimal(quantity));
        }

        public static Map<Integer, BigDecimal> calculatedAmountForFeeding(List<FeedingEntity> feedingEntities,
                        List<FeedingItemsGrouped> feedingItemsGroupeds, Integer quantityAdultReserve,
                        Integer quantityAdultExtraReserve, Integer quantityAdultMayorReserve,
                        Integer quantityKidReserve) {
                Map<Integer, BigDecimal> map = new HashMap<>();
                for (FeedingEntity feedingEntity : feedingEntities) {
                        BigDecimal total = BigDecimal.ZERO;
                        List<FeedingItemsGrouped> feedingItemsGrouped = feedingItemsGroupeds.stream()
                                        .filter(f -> f.getIdfeeding().equals(feedingEntity.getId()))
                                        .toList();
                        Float valueAdultMayor = feedingItemsGrouped.stream()
                                        .filter(d -> d.getName()
                                                        .equals("Adulto Mayor"))
                                        .map(FeedingItemsGrouped::getValue)
                                        .findFirst()
                                        .orElse(0f);
                        Float valueAdult = feedingItemsGrouped.stream()
                                        .filter(d -> d.getName()
                                                        .equals("Adulto"))
                                        .map(FeedingItemsGrouped::getValue)
                                        .findFirst()
                                        .orElse(0f);
                        Float valueKid = feedingItemsGrouped.stream()
                                        .filter(d -> d.getName().equals("Niño"))
                                        .map(FeedingItemsGrouped::getValue)
                                        .findFirst()
                                        .orElse(0f);
                        Float valueAdultExtra = feedingItemsGrouped.stream()
                                        .filter(d -> d.getName()
                                                        .equals("Adulto Extra"))
                                        .map(FeedingItemsGrouped::getValue)
                                        .findFirst()
                                        .orElse(0f);
                        Float costAdultMayor = GeneralMethods.calculatedFeeding(
                                        valueAdultMayor,
                                        quantityAdultMayorReserve)
                                        .floatValue();
                        Float costAdult = GeneralMethods
                                        .calculatedFeeding(valueAdult,
                                                        quantityAdultReserve)
                                        .floatValue();
                        Float costKid = GeneralMethods.calculatedFeeding(
                                        valueKid,
                                        quantityKidReserve)
                                        .floatValue();
                        Float costAdultExtra = GeneralMethods
                                        .calculatedFeeding(valueAdultExtra,
                                                        quantityAdultExtraReserve)
                                        .floatValue();
                        total = total
                                        .add(new BigDecimal(costAdult))
                                        .add(new BigDecimal(costAdultExtra))
                                        .add(new BigDecimal(costAdultMayor))
                                        .add(new BigDecimal(costKid));
                        map.put(feedingEntity.getId(), total);
                }
                return map;
        }

        public static BigDecimal calculatedTotalAmountFeeding(List<FeedingEntity> feedingEntities,
                        List<FeedingItemsGrouped> feedingItemsGroupeds, Integer quantityAdultReserve,
                        Integer quantityAdultExtraReserve, Integer quantityAdultMayorReserve,
                        Integer quantityKidReserve) {
                BigDecimal total = BigDecimal.ZERO;
                for (FeedingEntity feedingEntity : feedingEntities) {
                        List<FeedingItemsGrouped> feedingItemsGrouped = feedingItemsGroupeds.stream()
                                        .filter(f -> f.getIdfeeding().equals(feedingEntity.getId()))
                                        .toList();
                        Float valueAdultMayor = feedingItemsGrouped.stream()
                                        .filter(d -> d.getName()
                                                        .equals("Adulto Mayor"))
                                        .map(FeedingItemsGrouped::getValue)
                                        .findFirst()
                                        .orElse(0f);
                        Float valueAdult = feedingItemsGrouped.stream()
                                        .filter(d -> d.getName()
                                                        .equals("Adulto"))
                                        .map(FeedingItemsGrouped::getValue)
                                        .findFirst()
                                        .orElse(0f);
                        Float valueKid = feedingItemsGrouped.stream()
                                        .filter(d -> d.getName().equals("Niño"))
                                        .map(FeedingItemsGrouped::getValue)
                                        .findFirst()
                                        .orElse(0f);
                        Float valueAdultExtra = feedingItemsGrouped.stream()
                                        .filter(d -> d.getName()
                                                        .equals("Adulto Extra"))
                                        .map(FeedingItemsGrouped::getValue)
                                        .findFirst()
                                        .orElse(0f);
                        Float costAdultMayor = GeneralMethods.calculatedFeeding(
                                        valueAdultMayor,
                                        quantityAdultMayorReserve)
                                        .floatValue();
                        Float costAdult = GeneralMethods
                                        .calculatedFeeding(valueAdult,
                                                        quantityAdultReserve)
                                        .floatValue();
                        Float costKid = GeneralMethods.calculatedFeeding(
                                        valueKid,
                                        quantityKidReserve)
                                        .floatValue();
                        Float costAdultExtra = GeneralMethods
                                        .calculatedFeeding(valueAdultExtra,
                                                        quantityAdultExtraReserve)
                                        .floatValue();
                        total = total
                                        .add(new BigDecimal(costAdult))
                                        .add(new BigDecimal(costAdultExtra))
                                        .add(new BigDecimal(costAdultMayor))
                                        .add(new BigDecimal(costKid));
                }
                return total;
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
