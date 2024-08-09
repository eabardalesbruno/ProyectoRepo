package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.ComplaintsBookEntity;
import reactor.core.publisher.Mono;

public interface ComplaintsBookService {
    Mono<ComplaintsBookEntity> createComplaint(ComplaintsBookEntity complaint);
}