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

    public Mono<UserEntity> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    public Mono<UserEntity> createUser(UserEntity user) {
        return userRepository.save(user);
    }

    public Mono<Void> deleteUser(Integer userId) {
        return userRepository.deleteById(userId);
    }

    public Mono<UserEntity> saveUser(UserEntity user) {
        return userRepository.save(user);
    }
}
