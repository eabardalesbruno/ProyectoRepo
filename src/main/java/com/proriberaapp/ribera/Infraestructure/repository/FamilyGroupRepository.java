package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.FeedingItemDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.DetailFoodDto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.proriberaapp.ribera.Domain.entities.FamilyGroupEntity;

import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface FamilyGroupRepository extends ReactiveCrudRepository<FamilyGroupEntity, Integer> {
    Flux<FamilyGroupEntity> findAllByStatus(Integer status);

    @Query("""
        select t.familygroupid, upper(concat(t.feeding_type,' ',t.family_group)) feeding, t.value cost
        from
        (select ftffg.value, ft.name feeding_type, fg.id familygroupid, fg.name family_group
        from feeding_type_feeding_family_group ftffg
        join feeding_type ft on ftffg.idfeedingtype = ft.id
        join family_group fg on ftffg.idfamilygroup = fg.id
        where ftffg.value > 0 and ftffg.idfeeding = 11
        order by ft.name, fg.name desc) t
        where t.familygroupid in (:typePersons)
    """)
    Flux<DetailFoodDto> getDetailFood(List<Integer> typePersons);
}
