package com.porpapps.moviesearchapi.controller;

import com.porpapps.moviesearchapi.client.TheMovieDbClient;
import com.porpapps.moviesearchapi.client.model.Provider;
import com.porpapps.moviesearchapi.client.model.QueryResult;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/tv-shows")
@RequiredArgsConstructor
@CrossOrigin(origins = {"${cross.origin.url}"})
public class TvShowController {

    private static final Logger LOG = LoggerFactory.getLogger(TvShowController.class);

    //private final TvShowService tvShowService;
    private final TheMovieDbClient movieDbClient;
    @Value("${moviedb.api.key}")
    private String movieDBApiKey;

    @GetMapping("/name/{tvShowName}")
    public QueryResult findTvShowsByName(@PathVariable String tvShowName) {
        LOG.info("findTvShowsByName : {}", tvShowName);
        return movieDbClient.searchTvShowByName(movieDBApiKey, tvShowName);
    }

    @GetMapping("/{tvShowId}")
    public Provider findProvidersByTvShowId(@PathVariable Integer tvShowId,
                                            @RequestParam(name = "showStreamingOnly", required = false) boolean isFlatrate) {
        LOG.info("findProvidersByTvShowId : {} {}", tvShowId, isFlatrate);
        //TvShow tvShow = tvShowService.findById(tvShowId);
        Provider provider;
        try {
            provider = movieDbClient.searchTvShowProvider(movieDBApiKey, tvShowId);
        } catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                LOG.info("findProvidersByTvShowId: TV Show not found with id : {}", tvShowId);
                return new Provider();
            }
            throw e;
        }
        provider.setLink("https://www.themoviedb.org/tv/" + tvShowId);
        //provider.setName(tvShow.getOriginalName());
        if (isFlatrate) {
            final var providers = new Provider();
            providers.setId(provider.getId());
            providers.setLink("https://www.themoviedb.org/tv/" + tvShowId);
            //providers.setName(tvShow.getOriginalName());
            for (final var result : provider.getResults().entrySet()) {
                if (result.getValue().getFlatrate() != null) {
                    providers.getResults().put(result.getKey(), result.getValue());
                }
            }
            return providers;
        }
        return provider;
    }
}
