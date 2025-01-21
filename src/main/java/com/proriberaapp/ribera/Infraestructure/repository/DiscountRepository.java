package com.proriberaapp.ribera.Infraestructure.repository;

import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.proriberaapp.ribera.Domain.entities.DiscountEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DiscountRepository extends R2dbcRepository<DiscountEntity, Integer> {

    @Query("""
            select COALESCE(d.percentage,0) as percentage,d.name,d.id from
            discount d
            join discount_item di on di.iddiscount=d.id
            where d.maxreservationnumber>(select count(dp.id) from discount_payment_book dp where dp.iddiscount=d.id and date_part('year', dp.createdat)=:year
                      and  dp.idclient=:idUser)
            and di.idpackage iN (:idPackage)
            and d.status=1
            GROUP BY d.name,d.id;
                    """)
    Mono<DiscountEntity> getDiscountWithItems(int idUser, List<Integer> idPackage);

    @Query("""
            select COALESCE(d.percentage,0) as percentage,d.name,d.id from
                      discount d
                      join discount_item di on di.iddiscount=d.id
                      where d.maxreservationnumber>(select count(dp.id) from discount_payment_book dp where dp.iddiscount=d.id and date_part('year', dp.createdat)=:year
                      and  dp.idclient=:idUser)
                      and di.idpackage iN (:idPackage)
                      and d.status=1
                      GROUP BY d.name,d.id;
                  """)
    Mono<DiscountEntity> getDiscountWithItemsAndYear(int idUser, List<Integer> idPackage, String year);
/* 
    @Query("""
            select COALESCE(d.percentage,0) as percentage,d.name,d.id from
                      discount d
                      join discount_item di on di.iddiscount=d.id
                      where d.maxreservationnumber>(select count(dp.id) from discount_payment_book dp where dp.iddiscount=d.id and date_part('year', dp.createdat)=date_part('year',CURRENT_DATE)
                      and  dp.idclient=:idUser)
                      and di.idpackage iN (:idPackage)
                      and d.status=1
                      GROUP BY d.name,d.id;
                  """)
    Mono<DiscountEntity> getDiscountWithItemsAndCurrentYear(int idUser, List<Integer> idPackage); */

    @Query("""
             select COALESCE(d.percentage,0) as percentage,d.name,d.id,d.applytofood,d.applytoreservation from
                               discount d
                               join discount_item di on di.iddiscount=d.id
                    join discount_item_inclub dic on dic."id"=di.iddiscountinclub
                               where
                                dic.idpackage in (:idPackage)
                               and d.status=1
                           GROUP BY d.percentage,d.name,d.id
                  """)
    Flux<DiscountEntity> getDiscountWithItemsAndCurrentYear(int idUser, List<Integer> idPackage);

    @Query("""
            INSERT INTO discount_payment_book (idclient,idpaymentbook, iddiscount, createdat)
            VALUES (:idClient,:idPaymentBook, :idDiscount, CURRENT_TIMESTAMP);
            """)
    Mono<Void> createInvoicePaymentBook(int idDiscount, int idPaymentBook, int idClient);

}
