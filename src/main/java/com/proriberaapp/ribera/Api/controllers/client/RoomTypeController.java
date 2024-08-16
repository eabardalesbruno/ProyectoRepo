package com.proriberaapp.ribera.Api.controllers.client;
import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.services.client.RoomTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/getalltypes")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping("/all")
    public Flux<RoomTypeEntity> getAllRoomTypes() {
        return roomTypeService.getAllRoomTypes();
    }
}