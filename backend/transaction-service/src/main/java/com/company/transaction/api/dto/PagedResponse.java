package com.company.transaction.api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Generic wrapper for paged responses")
public record PagedResponse<T>(
        List<T> items,
        long totalElements,
        int page,
        int size
) {
}
