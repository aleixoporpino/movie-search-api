package com.porpapps.moviesearchapi.model;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@Entity
@Table(name = "quote", schema = "moviesdb")
public class Quote {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "phrase")
    private String phrase;

    @Column(name = "author")
    private String author;
}
