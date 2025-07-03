package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Data;
import java.math.BigDecimal;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@NoArgsConstructor
@Data
public class WithdrawRequestDTO {
    @NotNull(message = "El ID de la wallet es obligatorio")
    private Integer walletId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal amount;

    @NotBlank(message = "El país es obligatorio")
    private String country;

    @NotBlank(message = "El banco es obligatorio")
    private String bank;

    @NotBlank(message = "El número de cuenta es obligatorio")
    private String accountNumber;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String documentType;

    @NotBlank(message = "El número de documento es obligatorio")
    private String documentNumber;

    @NotBlank(message = "El nombre del titular es obligatorio")
    private String holderFirstName;

    @NotBlank(message = "El apellido del titular es obligatorio")
    private String holderLastName;
}
