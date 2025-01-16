package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import com.proriberaapp.ribera.Domain.dto.FeedingDto;
import com.proriberaapp.ribera.Domain.entities.FeedingEntity;

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
    String title;
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
    // Boolean cancelledAnticipated,
    // @Column("daybookinginit")
    List<ComfortData> ListComfortType;
    // @Column("daybookingend")
    List<BedsType> ListBedsType;
    List<BookingFeedingDto> listFeeding;
    @Column("daybookinginit")
    Timestamp dayBookingInit;
    @Column("daybookingend")
    Timestamp dayBookingEnd;
    BigDecimal price;
    @Column("pointsinresort")
    Integer pointsInResort;
    @Column("pointsribera")
    Integer pointsRibera;
    @Column("userclientid")
    Integer userClientId;
    @Column("userpromotorId")
    Integer userPromotorId;
    @Column("roomofferid")
    Integer roomOfferId;

    @Getter
    @Setter
    @Builder
    public static class ComfortData {
        @Column("comforttypename")
        String comfortTypeName;
        @Column("comforttypedescription")
        String comfortTypeDescription;
        @Column("bookingid")
        Integer bookingId;
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
        @Column("bookingid")
        Integer bookingId;
    }
}
