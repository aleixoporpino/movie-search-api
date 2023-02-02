package com.porpapps.moviesearchapi.client.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class CountryResult {
    private List<Rent> rent;
    private List<Buy> buy;
    private List<Flatrate> flatrate;
}
