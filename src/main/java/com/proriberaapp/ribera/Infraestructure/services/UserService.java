package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Flux<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<UserEntity> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Mono<UserEntity> createUser(UserEntity user) {
        return userRepository.save(user);
    }

    public Mono<Void> deleteUser(Integer id) {
        return userRepository.deleteById(id);
    }

    public Mono<UserEntity> updateUser(Integer id, UserEntity user) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    return userRepository.save(existingUser);
                });
    }
}