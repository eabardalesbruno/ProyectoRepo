package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.FeedingItemsGrouped;
import com.proriberaapp.ribera.Domain.entities.FeedingEntity;

import reactor.core.publisher.Flux;

import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedingRepository extends ReactiveCrudRepository<FeedingEntity, Integer> {
    @Query("SELECT * FROM feeding WHERE state = :state")
    Flux<FeedingEntity> findAllState(Integer state);

    
        @Query("""
                        select ftg.idfeeding,sum(ftg."value") as value, fg."name" as name from feeding_type_feeding_family_group ftg
                                 join feeding_type ft on ft."id"=ftg.idfeedingtype
                                 join family_group fg on fg."id"=ftg.idfamilygroup
                                  where ftg.idfeeding in (:idfeeding)
                                 GROUP BY fg."name",   ftg.idfeeding;

                         """)
        Flux<FeedingItemsGrouped> groupingByFamilyGroup(List<Integer> idfeeding);

}
