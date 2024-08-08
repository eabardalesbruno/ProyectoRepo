package com.proriberaapp.ribera.Api.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proriberaapp.ribera.Domain.entities.ComplaintsBookEntity;
import com.proriberaapp.ribera.services.client.ComplaintsBookService;

@RestController
@RequestMapping("/api/v1/complaints")
public class ComplaintsBookController {

    private final ComplaintsBookService complaintsBookService;

    @Autowired
    public ComplaintsBookController(ComplaintsBookService complaintsBookService) {
        this.complaintsBookService = complaintsBookService;
    }

    @PostMapping
    public ResponseEntity<ComplaintsBookEntity> createComplaint(@RequestBody ComplaintsBookEntity complaint) {
        ComplaintsBookEntity savedComplaint = complaintsBookService.createComplaint(complaint);
        return new ResponseEntity<>(savedComplaint, HttpStatus.CREATED);
    }
}
