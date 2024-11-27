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
            select COALESCE(SUM(DISTINCT d.percentage),0) as percentage,d.name,d.id from
            discount d
            join discount_item di on di.iddiscount=d.id
            where d.maxreservationnumber>(select count(pb.paymentbookid) from paymentbook pb
            where pb.userclientid=:idUser and pb.applydiscount=true)
            and di.idpackage iN (:idPackage)
            and d.status=1
            GROUP BY d.name,d.id;
                    """)
    Mono<DiscountEntity> getPercentajeWithItemsDiscount(int idUser, List<Integer> idPackage);

}
