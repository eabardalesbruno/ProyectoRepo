package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.DetailComplaintsEntity;

import java.util.List;
import java.util.Optional;

public interface DetailComplaintsService {
    Optional<DetailComplaintsEntity> findById(Integer id);
    List<DetailComplaintsEntity> findAll();
    DetailComplaintsEntity save(DetailComplaintsEntity detailComplaintsEntity);
    void deleteById(Integer id);
}
