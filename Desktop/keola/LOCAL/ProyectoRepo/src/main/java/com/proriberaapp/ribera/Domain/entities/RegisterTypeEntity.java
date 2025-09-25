package com.proriberaapp.ribera.Domain.entities;

import com.proriberaapp.ribera.Api.controllers.admin.dto.RegisterTypeRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Flux;

@Getter
@Setter
@Builder
@Table("registertype")
public class RegisterTypeEntity {
    @Id
    @Column("registertypeid")
    private Integer registerTypeId;
    @Column("registertypename")
    private String registerTypeName;

    public static Flux<RegisterTypeEntity> toEntity(Flux<RegisterTypeRequest> registerTypeRequest) {
        return registerTypeRequest.map(RegisterTypeRequest::toEntity);
    }
}
