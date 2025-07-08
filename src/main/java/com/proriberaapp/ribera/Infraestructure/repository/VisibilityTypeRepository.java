package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.response.DropDownVisibilityTypeResponse;
import com.proriberaapp.ribera.Domain.entities.VisibilityTypeEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface VisibilityTypeRepository extends R2dbcRepository<VisibilityTypeEntity,Integer> {

    @Query(value = """
            SELECT
                vt.id_visibility_type AS idvisibilitytype,
                vt.visibility_name AS visibilitytypename
            FROM visibility_type vt
            WHERE
                vt.status = 1
                AND (
                    (:searchTerm IS NULL)
                    OR (
                        TRIM(UPPER(vt.visibility_name)) LIKE '%' || TRIM(UPPER(:searchTerm)) || '%'
                    )
                )
            ORDER BY vt.id_visibility_type ASC;
            """)
    Flux<DropDownVisibilityTypeResponse>getDropDownVisivility(String searchTerm);
}
