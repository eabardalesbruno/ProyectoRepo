package com.proriberaapp.ribera.Api.controllers.admin;
import com.proriberaapp.ribera.Api.controllers.admin.dto.ViewRoomReturn;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/v1/admin/manager/room")
@RequiredArgsConstructor
@Slf4j
public class ManagerRoomController extends BaseManagerController<RoomEntity, RoomEntity> {

    private final RoomService roomService;


    //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/find/all/view")
    public ResponseEntity<Flux<ViewRoomReturn>> findAllViewRoomReturn() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*"); // Permitir desde cualquier origen

        return ResponseEntity.ok().headers(headers).body(roomService.findAllView());
    }
}
