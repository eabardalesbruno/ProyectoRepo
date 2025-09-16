package com.proriberaapp.ribera.Domain.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PaginationDTO {
    private Integer currentPage;
    private Integer totalPages;
    private Long totalElements;
    private Integer size;
}
