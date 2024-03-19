package com.proriberaapp.ribera.Infraestructure.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BaseService<R,T> {
    Mono<R> save(T entity);
    Flux<R> saveAll(List<T> entity);
    Mono<R> findById(Integer id);
    Flux<R> findAll();
    Mono<Void> deleteById(Integer id);
    Mono<R> update(T entity);

}
