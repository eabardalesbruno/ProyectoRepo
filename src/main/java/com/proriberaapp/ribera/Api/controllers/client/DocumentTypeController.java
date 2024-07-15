package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.DocumentTypeEntity;
import com.proriberaapp.ribera.services.client.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documenttype")
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    @Autowired
    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    @PostMapping("/createTable")
    public void createDocumentTypeTable() {
        documentTypeService.createDocumentTypeTable();
    }

    @GetMapping("/all")
    public Flux<DocumentTypeEntity> getAllDocumentTypes() {
        return documentTypeService.getAllDocumentTypes();
    }
}
