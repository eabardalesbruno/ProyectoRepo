package com.proriberaapp.ribera.services.client.impl;
import com.proriberaapp.ribera.Domain.entities.RoomStateEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RoomStateRepository;
import com.proriberaapp.ribera.services.client.RoomStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoomStateServiceImpl implements RoomStateService {

    private final RoomStateRepository roomStateRepository;

    @Override
    public Mono<RoomStateEntity> createRoomState(RoomStateEntity roomStateEntity) {
        return roomStateRepository.save(roomStateEntity);
    }

    @Override
    public Mono<RoomStateEntity> updateRoomState(Integer id, RoomStateEntity roomStateEntity) {
        return roomStateRepository.findById(id)
                .flatMap(existingState -> {
                    existingState.setRoomStateName(roomStateEntity.getRoomStateName());
                    existingState.setRoomStateDescription(roomStateEntity.getRoomStateDescription());
                    return roomStateRepository.save(existingState);
                });
    }

    @Override
    public Mono<Void> deleteRoomState(Integer id) {
        return roomStateRepository.deleteById(id);
    }

    @Override
    public Flux<RoomStateEntity> getAllRoomStates() {
        return roomStateRepository.findAll();
    }
}
