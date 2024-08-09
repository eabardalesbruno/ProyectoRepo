package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.DetailComplaintsEntity;
import com.proriberaapp.ribera.Infraestructure.repository.DetailComplaintsRepository;
import com.proriberaapp.ribera.services.client.DetailComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetailComplaintsServiceImpl implements DetailComplaintsService {

    private final DetailComplaintsRepository detailComplaintsRepository;

    @Autowired
    public DetailComplaintsServiceImpl(DetailComplaintsRepository detailComplaintsRepository) {
        this.detailComplaintsRepository = detailComplaintsRepository;
    }

    @Override
    public Optional<DetailComplaintsEntity> findById(Integer id) {
        return detailComplaintsRepository.findById(id).blockOptional();
    }

    @Override
    public List<DetailComplaintsEntity> findAll() {
        return detailComplaintsRepository.findAll().collectList().block();
    }

    @Override
    public DetailComplaintsEntity save(DetailComplaintsEntity detailComplaintsEntity) {
        return detailComplaintsRepository.save(detailComplaintsEntity).block();
    }

    @Override
    public void deleteById(Integer id) {
        detailComplaintsRepository.deleteById(id).block();
    }
}
