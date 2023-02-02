package com.porpapps.moviesearchapi.client.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class MovieResult {
    private Integer id;

    @JsonAlias(value = "original_title")
    private String originalTitle;

    @JsonAlias(value = "poster_path")
    private String posterPath;

    @JsonAlias(value = "release_date")
    private String releaseDate;

    private String overview;
}
