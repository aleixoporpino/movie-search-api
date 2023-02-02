package com.porpapps.moviesearchapi.client.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class QueryResult {
    private Integer page;
    private List<MovieResult> results;
    @JsonAlias(value = "total_pages")
    private Integer totalPages;
    @JsonAlias(value = "total_results")
    private Integer totalResults;
}
