package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Table("feeding_type_feeding_family_group")
public class FeedingTypeFeedingGroupAndFeedingEntity {
    @Id
    private Integer id;
    private Integer idfamilygroup;
    private float value;
    private Integer idfeedingtype;
    private Integer idfeeding;
}
