package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.UserClientDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UserClientPageDto;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.admin.ClientManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientManagerServiceImpl implements ClientManagerService {
    private final UserClientRepository userClientRepository;
    @Override
    public Mono<UserClientEntity> updatePassword(Integer id, String newPassword) {
        return userClientRepository.findById(id).map(user -> {
            user.setPassword(newPassword);
            return user;
        }).flatMap(userClientRepository::save);
    }

    @Override
    public Mono<UserClientEntity> updatePassword(Integer id, String newPassword, String oldPassword) {
        return userClientRepository.findById(id).map(user -> {
            user.setPassword(newPassword);
            return user;
        }).flatMap(userClientRepository::save);
    }

    @Override
    public Mono<UserClientEntity> requestUpdatePassword(String email) {
        return userClientRepository.findByEmail(email);
    }

    @Override
    public Mono<UserClientEntity> findByEmail(String email) {
        return userClientRepository.findByEmail(email);
    }

    @Override
    public Mono<UserClientEntity> findById(Integer id) {
        return userClientRepository.findById(id);
    }

    @Override
    public Flux<UserClientEntity> findAll() {
        return userClientRepository.findAll();
    }

    @Override
    public Mono<UserClientEntity> disable(Integer id) {
        return userClientRepository.findById(id).map(user -> {
            user.setStatus(StatesUser.INACTIVE);
            return user;
        }).flatMap(userClientRepository::save);
    }

    @Override
    public Mono<UserClientEntity> enable(Integer id) {
        return userClientRepository.findById(id).map(user -> {
            user.setStatus(StatesUser.ACTIVE);
            return user;
        }).flatMap(userClientRepository::save);
    }

    @Override
    public Mono<UserClientEntity> delete(Integer id) {
        return userClientRepository.findById(id).map(user -> {
            user.setStatus(StatesUser.DELETED);
            return user;
        }).flatMap(userClientRepository::save);
    }

    @Override
    public Mono<UserClientPageDto> getAllClients(Integer indice, Integer statusId, String fecha, String filter) {
        UserClientPageDto resp = new UserClientPageDto();
        return userClientRepository.countUserAll(statusId, fecha, filter).flatMap(totalClients -> {
            resp.setTotalClients(totalClients);
            return userClientRepository.getAllClients(indice, statusId, fecha, filter).collectList().map(userClientDtos -> {
                resp.setUsersClients(userClientDtos);
                return resp;
            });
        });
    }

    @Override
    public Mono<Void> saveClient(UserClientDto entity) {
        return userClientRepository.findByUserClientId(entity.getUserClientId()).flatMap(userClientEntity -> {
            userClientEntity.setFirstName(entity.getFirstName());
            userClientEntity.setLastName(entity.getLastName());
            userClientEntity.setEmail(entity.getEmail());
            userClientEntity.setCellNumber(entity.getCellNumber());
            userClientEntity.setDocumentNumber(entity.getDocumentNumber());
            userClientEntity.setGenderId(entity.getGenderId());
            userClientEntity.setBirthDate(entity.getBirthDate());
            userClientEntity.setCountryId(entity.getCountryId());
            userClientEntity.setAddress(entity.getAddress());
            return userClientRepository.save(userClientEntity).flatMap(resp -> {
                return Mono.empty();
            });
        });
    }

}
