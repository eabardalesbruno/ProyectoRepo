package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.DetailComplaintsEntity;
import com.proriberaapp.ribera.services.client.DetailComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detailcomplaints")
public class DetailComplaintsController {

    private final DetailComplaintsService detailComplaintsService;

    @Autowired
    public DetailComplaintsController(DetailComplaintsService detailComplaintsService) {
        this.detailComplaintsService = detailComplaintsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailComplaintsEntity> getDetailComplaintById(@PathVariable Integer id) {
        return detailComplaintsService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DetailComplaintsEntity>> getAllDetailComplaints() {
        List<DetailComplaintsEntity> detailComplaints = detailComplaintsService.findAll();
        return ResponseEntity.ok(detailComplaints);
    }

    @PostMapping
    public ResponseEntity<DetailComplaintsEntity> createDetailComplaint(@RequestBody DetailComplaintsEntity detailComplaintsEntity) {
        DetailComplaintsEntity savedEntity = detailComplaintsService.save(detailComplaintsEntity);
        return ResponseEntity.ok(savedEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetailComplaintsEntity> updateDetailComplaint(@PathVariable Integer id, @RequestBody DetailComplaintsEntity detailComplaintsEntity) {
        if (!detailComplaintsService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        detailComplaintsEntity.setDetailComplaintsId(id);
        DetailComplaintsEntity updatedEntity = detailComplaintsService.save(detailComplaintsEntity);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetailComplaint(@PathVariable Integer id) {
        if (!detailComplaintsService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        detailComplaintsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
