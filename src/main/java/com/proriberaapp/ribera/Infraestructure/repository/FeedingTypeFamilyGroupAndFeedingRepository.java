package com.proriberaapp.ribera.Infraestructure.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.FeedingItemDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.FeedingItemsGrouped;
import com.proriberaapp.ribera.Domain.entities.FeedingTypeFeedingGroupAndFeedingEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FeedingTypeFamilyGroupAndFeedingRepository
                extends ReactiveCrudRepository<FeedingTypeFeedingGroupAndFeedingEntity, Integer> {

        @Query("DELETE FROM feeding_type_feeding_family_group WHERE idfeeding = :idfeeding")
        Mono<Void> deleteByIdfeeding(Integer idfeeding);

        @Query("""
                        select ff.*,f.feedingname,ft."name" as feedingtypename,fg."name" as feedinggroupname,fg."name" as familygroupname from feeding_type_feeding_family_group ff
                        join feeding f on f."id"=ff.idfeeding
                        join feeding_type ft on ft."id"=ff.idfeedingtype
                        join family_group fg on fg."id"=ff.idfamilygroup
                                where ff.idfeeding=:idfeeding
                                order by id desc;
                        """)
        Flux<FeedingItemDto> findByIdfeeding(Integer idfeeding);

        @Query("""
                        select sum(ftg."value"), fg."name" from feeding_type_feeding_family_group ftg
                                 join feeding_type ft on ft."id"=ftg.idfeedingtype
                                 join family_group fg on fg."id"=ftg.idfamilygroup
                                  where ftg.idfeeding=:idfeeding
                                 GROUP BY fg."name";

                         """)
        Flux<FeedingItemsGrouped> groupingByFamilyGroup(Integer idfeeding);

}
