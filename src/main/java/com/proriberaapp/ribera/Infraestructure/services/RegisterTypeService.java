package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Api.controllers.admin.dto.RegisterTypeRequest;
import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RegisterTypeService extends BaseService<RegisterTypeEntity,RegisterTypeRequest> {

}
