package com.porpapps.moviesearchapi.client.model;

import lombok.*;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Provider {
    private Integer id;
    private String name;
    private String link;
    private Map<String, CountryResult> results = new LinkedCaseInsensitiveMap<>();
}
