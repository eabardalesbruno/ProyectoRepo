package com.proriberaapp.ribera.Api.controllers.client.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proriberaapp.ribera.Domain.enums.RewardStatus;
import com.proriberaapp.ribera.Domain.enums.RewardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRewardResponse {
    private Long id;
    private Long userId;
    private Integer points;

    @JsonIgnore
    private LocalDateTime date;

    private RewardType type;
    private Boolean active;

    @JsonIgnore
    private LocalDateTime expirationDate;

    private RewardStatus rewardStatus;

    @JsonGetter("date")
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMM. yyyy", new Locale("es", "ES"));
        return date != null ? date.format(formatter) : null;
    }

    @JsonGetter("expirationDate")
    public String getFormattedExpirationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMM. yyyy", new Locale("es", "ES"));
        return expirationDate != null ? expirationDate.format(formatter) : null;
    }

    @JsonGetter("description")
    public String getRewardDescription() {
        if (type == null) return "Otro";

        return switch (type) {
            case BOOKING -> "Recompensa por reserva";
            case REFERRAL -> "Recompensa por referir a un amigo";
            case LOGIN -> "Recompensa por iniciar sesión";
            case REVIEW -> "Recompensa por dejar una reseña";
            case BIRTHDAY -> "Recompensa de cumpleaños";
            case PROMOTION -> "Recompensa promocional";
            case OTHER -> "Otro";
        };
    }
}
