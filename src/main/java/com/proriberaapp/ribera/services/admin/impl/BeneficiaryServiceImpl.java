package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Domain.dto.BeneficiaryDto;
import com.proriberaapp.ribera.Domain.entities.BeneficiaryEntity;
import com.proriberaapp.ribera.services.admin.BeneficiaryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BeneficiaryServiceImpl implements BeneficiaryService {
    private final com.proriberaapp.ribera.Infraestructure.repository.BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryServiceImpl(
            com.proriberaapp.ribera.Infraestructure.repository.BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    @Override
    public Mono<BeneficiaryDto> createBeneficiary(BeneficiaryDto dto) {
        BeneficiaryEntity entity = toEntity(dto);
        log.info("Intentando guardar beneficiario: {}", entity);
        return beneficiaryRepository.save(entity)
                .doOnSuccess(saved -> log.info("Beneficiario guardado: {}", saved))
                .doOnError(error -> log.error("Error al guardar beneficiario", error))
                .map(this::toDto);
    }

    @Override
    public Mono<BeneficiaryDto> updateBeneficiary(Integer id, BeneficiaryDto dto) {
        return beneficiaryRepository.findById(id)
                .flatMap(existing -> {
                    BeneficiaryEntity updated = toEntity(dto);
                    updated.setId(id);
                    log.info("Actualizando beneficiario id {}: {}", id, updated);
                    return beneficiaryRepository.save(updated)
                            .doOnSuccess(saved -> log.info("Beneficiario actualizado: {}", saved))
                            .doOnError(error -> log.error("Error al actualizar beneficiario", error));
                })
                .map(this::toDto);
    }

    @Override
    public Mono<Void> deleteBeneficiary(Integer id) {
        log.info("Eliminando beneficiario id {}", id);
        return beneficiaryRepository.deleteById(id)
                .doOnSuccess(v -> log.info("Beneficiario eliminado id {}", id))
                .doOnError(error -> log.error("Error al eliminar beneficiario", error));
    }

    @Override
    public Mono<BeneficiaryDto> getBeneficiaryById(Integer id) {
        log.info("Buscando beneficiario id {}", id);
        return beneficiaryRepository.findById(id)
                .doOnError(error -> log.error("Error al buscar beneficiario", error))
                .map(this::toDto);
    }

    @Override
    public Flux<BeneficiaryDto> getAllBeneficiaries() {
        log.info("Listando todos los beneficiarios");
        return beneficiaryRepository.findAll()
                .doOnError(error -> log.error("Error al listar beneficiarios", error))
                .map(this::toDto);
    }

    @Override
    public Flux<BeneficiaryDto> filterBeneficiaries(String nombre, String membresia) {
        log.info("Filtrando beneficiarios por nombre: {} y membresía: {}", nombre, membresia);
        if (nombre != null && membresia != null) {
            return beneficiaryRepository
                    .findByNombresContainingIgnoreCaseAndMembresiaContainingIgnoreCase(nombre, membresia)
                    .doOnError(error -> log.error("Error al filtrar beneficiarios", error))
                    .map(this::toDto);
        } else if (nombre != null) {
            return beneficiaryRepository.findByNombresContainingIgnoreCase(nombre)
                    .doOnError(error -> log.error("Error al filtrar beneficiarios", error))
                    .map(this::toDto);
        } else if (membresia != null) {
            return beneficiaryRepository.findByMembresiaContainingIgnoreCase(membresia)
                    .doOnError(error -> log.error("Error al filtrar beneficiarios", error))
                    .map(this::toDto);
        } else {
            return getAllBeneficiaries();
        }
    }

    private BeneficiaryEntity toEntity(BeneficiaryDto dto) {
        BeneficiaryEntity entity = new BeneficiaryEntity();
        entity.setId(dto.getId());
        entity.setNombres(dto.getNombres());
        entity.setApellidos(dto.getApellidos());
        entity.setDocumento(dto.getDocumento());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setEdad(dto.getEdad());
        entity.setCorreo(dto.getCorreo());
        entity.setVisitas(dto.getVisitas());
        entity.setMembresia(dto.getMembresia());
        entity.setUsuario(dto.getUsuario());
        entity.setEstado(dto.getEstado());
        return entity;
    }

    private BeneficiaryDto toDto(BeneficiaryEntity entity) {
        return BeneficiaryDto.builder()
                .id(entity.getId())
                .nombres(entity.getNombres())
                .apellidos(entity.getApellidos())
                .documento(entity.getDocumento())
                .fechaNacimiento(entity.getFechaNacimiento())
                .edad(entity.getEdad())
                .correo(entity.getCorreo())
                .visitas(entity.getVisitas())
                .membresia(entity.getMembresia())
                .usuario(entity.getUsuario())
                .estado(entity.getEstado())
                .build();
    }
}
