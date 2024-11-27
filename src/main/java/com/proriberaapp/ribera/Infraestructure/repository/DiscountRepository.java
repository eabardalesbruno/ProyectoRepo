package com.proriberaapp.ribera.Infraestructure.repository;

import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.proriberaapp.ribera.Domain.entities.DiscountEntity;

import reactor.core.publisher.Mono;

@Repository
public interface DiscountRepository extends R2dbcRepository<DiscountEntity, Integer> {

    @Query("""
            SELECT COALESCE(d.percentaje,0) as percentage,d.name FROM discount d
            join discount_item item on d.id = item.id_discount
            WHERE item.idPackage IN :idPackage
            limit 1
                    """)
    Mono<DiscountEntity> getPercentajeWithItemsDiscount(List<Integer> idPackage);

}
