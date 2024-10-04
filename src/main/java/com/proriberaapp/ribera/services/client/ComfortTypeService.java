package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.ComfortTypeEntity;
import com.proriberaapp.ribera.services.BaseService;
import reactor.core.publisher.Mono;

public interface ComfortTypeService extends BaseService<ComfortTypeEntity, ComfortTypeEntity> {
    Mono<Void> deleteComforTypeById(Integer id);
}
