package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.services.client.ComplaintsBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proriberaapp.ribera.Infraestructure.repository.ComplaintsBookRepository;
import com.proriberaapp.ribera.Domain.entities.ComplaintsBookEntity;
import reactor.core.publisher.Mono;

@Service
public class ComplaintsBookServiceImpl implements ComplaintsBookService {

    private final ComplaintsBookRepository complaintsBookRepository;

    @Autowired
    public ComplaintsBookServiceImpl(ComplaintsBookRepository complaintsBookRepository) {
        this.complaintsBookRepository = complaintsBookRepository;
    }

    @Override
    public Mono<ComplaintsBookEntity> createComplaint(ComplaintsBookEntity complaint) {
        return complaintsBookRepository.save(complaint);
    }
}
