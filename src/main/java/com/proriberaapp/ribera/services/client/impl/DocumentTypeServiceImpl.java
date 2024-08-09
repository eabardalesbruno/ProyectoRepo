package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.DocumentTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.DocumentTypeRepository;
import com.proriberaapp.ribera.services.client.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    @Autowired
    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public void createDocumentTypeTable() {
        // Aquí se puede agregar la lógica para crear la tabla documenttype si no existe
        // Sin embargo, en JPA/Hibernate, la tabla se crea automáticamente si la entidad está correctamente mapeada
        // y la base de datos está configurada adecuadamente.
    }

    @Override
    public Flux<DocumentTypeEntity> getAllDocumentTypes() {
        return documentTypeRepository.findAll();
    }
}