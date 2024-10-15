package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
public class ViewBookingReturn {
    @Column("bookingid")
    Integer bookingId;
    String image;
    String state;
    Integer roomid;
    String description;
    Integer bedrooms;
    @Column("squaremeters")
    String squareMeters;
    @Column("oceanviewbalcony")
    Boolean oceanViewBalcony;
    @Column("balconyoverlookingpool")
    Boolean balconyOverLookingPool;
    Integer numberadults;
    Integer numberchildren;
    Integer numberbabies;
    Integer numberadultsextra;
    Integer numberadultsmayor;
    //Boolean cancelledAnticipated,
    //@Column("daybookinginit")
    List<ComfortData> ListComfortType;
    //@Column("daybookingend")
    List<BedsType> ListBedsType;
    @Column("daybookinginit")
    Timestamp dayBookingInit;
    @Column("daybookingend")
    Timestamp dayBookingEnd;
    BigDecimal price;
    @Column("pointsinresort")
    Integer pointsInResort;
    @Column("pointsribera")
    Integer pointsRibera;

    @Getter
    @Setter
    @Builder
    public static class ComfortData {
        @Column("comforttypename")
        String comfortTypeName;
        @Column("comforttypedescription")
        String comfortTypeDescription;
    }

    @Getter
    @Setter
    @Builder
    public static class BedsType {
        @Column("bedtypename")
        String bedTypeName;
        @Column("bedtypedescription")
        String bedTypeDescription;
        Integer quantity;
    }
}
