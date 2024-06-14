package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.PointsExchangeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PointsExchangeRepository;
import com.proriberaapp.ribera.services.client.PointsExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PointsExchangeServiceImpl implements PointsExchangeService {
    private final PointsExchangeRepository pointsExchangeRepository;

    @Autowired
    public PointsExchangeServiceImpl(PointsExchangeRepository pointsExchangeRepository) {
        this.pointsExchangeRepository = pointsExchangeRepository;
    }

    @Override
    public Mono<PointsExchangeEntity> createPointsExchange(PointsExchangeEntity pointsExchange) {
        return pointsExchangeRepository.save(pointsExchange);
    }

    @Override
    public Mono<PointsExchangeEntity> getPointsExchangeById(Integer id) {
        return pointsExchangeRepository.findById(id);
    }

    @Override
    public Flux<PointsExchangeEntity> getAllPointsExchanges() {
        return pointsExchangeRepository.findAll();
    }

    @Override
    public Mono<PointsExchangeEntity> updatePointsExchange(Integer id, PointsExchangeEntity pointsExchange) {
        return pointsExchangeRepository.findById(id)
                .flatMap(existingPointsExchange -> {
                    // Update properties
                    existingPointsExchange.setUserclientid(pointsExchange.getUserclientid());
                    existingPointsExchange.setExchangetypeid(pointsExchange.getExchangetypeid());
                    existingPointsExchange.setDateuse(pointsExchange.getDateuse());
                    existingPointsExchange.setExchangecode(pointsExchange.getExchangecode());
                    existingPointsExchange.setServiceid(pointsExchange.getServiceid());
                    existingPointsExchange.setDescription(pointsExchange.getDescription());
                    existingPointsExchange.setCheckin(pointsExchange.getCheckin());
                    existingPointsExchange.setCheckout(pointsExchange.getCheckout());
                    existingPointsExchange.setNights(pointsExchange.getNights());
                    existingPointsExchange.setPointsquantity(pointsExchange.getPointsquantity());
                    existingPointsExchange.setPointsused(pointsExchange.getPointsused());
                    existingPointsExchange.setPointstypeid(pointsExchange.getPointstypeid());
                    existingPointsExchange.setBookingid(pointsExchange.getBookingid());
                    return pointsExchangeRepository.save(existingPointsExchange);
                });
    }

    @Override
    public Mono<Void> deletePointsExchange(Integer id) {
        return pointsExchangeRepository.deleteById(id);
    }
}
