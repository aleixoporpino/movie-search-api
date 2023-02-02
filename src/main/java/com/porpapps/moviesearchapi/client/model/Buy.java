package com.porpapps.moviesearchapi.client.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Buy {
    @JsonAlias(value = "provider_name")
    private String providerName;
    @JsonAlias(value = "logo_path")
    private String logoPath;
}
