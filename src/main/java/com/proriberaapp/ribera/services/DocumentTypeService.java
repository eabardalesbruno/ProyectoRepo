package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.DocumentTypeEntity;

import java.util.List;

public interface DocumentTypeService {
    void createDocumentTypeTable();
    List<DocumentTypeEntity> getAllDocumentTypes();
}