package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.DocumentTypeEntity;
import reactor.core.publisher.Flux;

import java.util.List;

public interface DocumentTypeService {
    void createDocumentTypeTable();
    Flux<DocumentTypeEntity> getAllDocumentTypes();
}