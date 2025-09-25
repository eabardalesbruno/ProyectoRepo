package com.proriberaapp.ribera.Domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PercentageData {
    @JsonProperty("package_detail_rewards")
    private List<PercentageDto> packageDetailRewards;
}
