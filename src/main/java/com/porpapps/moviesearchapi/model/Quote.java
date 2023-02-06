package com.porpapps.moviesearchapi.model;

import lombok.*;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@AllArgsConstructor
//@Entity
//@Table(name = "quote", schema = "moviesdb")
public class Quote {

    // private Integer id;

    private String phrase;

    private String author;
}
