package com.proriberaapp.ribera.Domain.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Table("feeding_type_feeding_family_group")
public class FeedingTypeFeedingGroupAndFeedingEntity {
    @Id
    private Integer id;
    private Integer idfamilygroup;
    private float value;
    private Integer idfeedingtype;
    private Integer idfeeding;
    @Transient
    private FeedingEntity feeding;
    @Transient
    private FeedingTypeEntity feedingType;
    @Transient
    private FamilyGroupEntity familyGroup;
}
