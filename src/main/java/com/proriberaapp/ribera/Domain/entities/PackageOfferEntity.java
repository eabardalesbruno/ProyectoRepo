package com.proriberaapp.ribera.Domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@Table("package_offer")
public class PackageOfferEntity {
    @Id
    private Integer packageOfferId;

    private Paquete paquete;
    private String offerName;

    private String urlImage;

    //private Integer roomTypeId;
    private HabitacionesTipo roomTypeHabitacion;
    private DepartamentoTipo roomTypeDepartamento;

    private List<Restaurante> listaRestaurant;

    private List<Bebidas> listaBebidas;

    private List<Entretenimiento> listaEntretenimiento;

    private List<Comodidades> listaComodidades;

    private String detalleDelPaquete;

    private String terminosCondiciones;

    private String politicasCancelacion;

    private LocalDate dateInit;
    private LocalDate dateEnd;
    private LocalTime timeInit;
    private LocalTime timeEnd;
    private Integer numberDays;
    private Integer numberNights;
    private Boolean isAllDay;

    private Integer adultos;
    private Integer ninos;
    private Integer infantes;

    private List<ServiciosAdicionales> listaServiciosAdicionales;

    private BigDecimal costAdulto;
    private BigDecimal costAdultoExtra;
    private BigDecimal costNino;

    private Boolean isSocio;
    private BigDecimal costAdultoCanje;
    private BigDecimal costAdultoExtraCanje;
    private BigDecimal costNinoCanje;

    private Boolean isPromocion;
    private List<Descuento> listaDescuentos;

    private Integer riberaPointsSemana;
    private Integer riberaPointsFinDeSemana;
    private Integer inResortPointsSemana;
    private Integer inResortPointsFinDeSemana;
    private Integer points;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Descuento {
        private TipoDescuento tipoDescuento;
        private BigDecimal discount;
        private String description;
    }

    @Getter
    public enum TipoDescuento {
        PORCENTAJE,
        MONTO
    }

    @Getter
    public enum HabitacionesTipo {
        HABITACIONES_DUPLEX("Habitaciones Duplex"),
        HABITACIONES_MATRIMONIALES("Habitaciones Matrimoniales");

        private final String value;

        HabitacionesTipo(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @Getter
    public enum DepartamentoTipo {
        DEPARTAMENTO_VISTA_PISCINA("Departamento vista piscina"),
        DEPARTAMMENTO_VISTA_JARDIN("Departamento vista jardín"),
        DEPARTAMENTO_VISTA_RIO("Departamento vista río");

        private final String value;

        DepartamentoTipo(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @Getter
    public enum Paquete {
        FULL_DAY("Full day"),
        CAMPING("Camping"),
        EVENTOS("Eventos");

        private final String value;

        Paquete(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @Getter
    public enum ServiciosAdicionales {
        KARAOKE("Karaoke"),
        ORQUESTA("Orquesta"),
        SHOW_INFANTIL("Show infantil"),
        CAMPING("Camping"),
        ANIMACION("Animación"),
        OTROS("Otros");

        private final String value;

        ServiciosAdicionales(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @Getter
    public enum Restaurante {
        DESAYUNO("Desayuno"),
        ENTRADAS("Entradas"),
        PLATOS_A_LA_CARTA("Platos a la carta"),
        TODOS("Todos");

        private final String value;

        Restaurante(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @Getter
    public enum Bebidas {
        DESAYUNO("Desayuno"),
        ENTRADAS("Entradas"),
        PLATOS_A_LA_CARTA("Platos a la carta"),
        TODOS("Todos");

        private final String value;

        Bebidas(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @Getter
    public enum Entretenimiento {
        YENGA_FAMILIAR("Yenga familiar"),
        TWISTER_GIGANTE("Twister gigante"),
        GYNCANA("Gyncana"),
        TIRA_DE_LA_CUERDA("Tira de la cuerda"),
        TALLER_DE_DIBUJO_LIBRE("Taller de dibujo libre"),
        FOLLOW_THE_LEADER("Follow the leader"),
        CARITAS_PINTADAS("Caritas pintadas"),
        VOLEY("Voley"),
        FUTBOL("Futbol"),
        PISCINA("Piscina"),
        AGUAGYM("AguaGym"),
        BINGO("Bingo"),
        TODAS_LAS_ACTIVIDADES_RECREATIVAS("Todas las actividades recreativas");

        private final String value;

        Entretenimiento(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
    @Getter
    public enum Comodidades {
        CARPA("Carpa"),
        HAMACA("Hamaca"),
        ESTACIONAMIENTO_24_HORAS("Estacionamiento 24 horas"),
        TODOS("Todos");

        private final String value;

        Comodidades(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}

