package com.proriberaapp.ribera.Domain.dto;

import com.proriberaapp.ribera.Domain.entities.CategoryRoomType;
import com.proriberaapp.ribera.Domain.entities.RoomStateEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomDto {

    private String roomNumber;

    private List<QuotationOffersDto> QuotationOffers;

}
