package com.porpapps.moviesearchapi.controller;

import com.porpapps.moviesearchapi.client.TheMovieDbClient;
import com.porpapps.moviesearchapi.client.model.Provider;
import com.porpapps.moviesearchapi.client.model.QueryResult;
import com.porpapps.moviesearchapi.utils.LogUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/tv-shows")
@RequiredArgsConstructor
@CrossOrigin(origins = {"${cross.origin.url}"})
public class TvShowController {
    //private final TvShowService tvShowService;
    private final TheMovieDbClient movieDbClient;
    @Value("${moviedb.api.key}")
    private String movieDBApiKey;

    @GetMapping("/name/{tvShowName}")
    public QueryResult findTvShowsByName(@PathVariable String tvShowName, HttpServletRequest request) {
        LogUtils.info(log, request, String.format("findTvShowsByName : %s", tvShowName));
        return movieDbClient.searchTvShowByName(movieDBApiKey, tvShowName);
    }

    @GetMapping("/{tvShowId}")
    public Provider findProvidersByTvShowId(@PathVariable Integer tvShowId,
                                            @RequestParam(name = "showStreamingOnly", required = false) boolean isFlatrate,
                                            HttpServletRequest request) {
        LogUtils.info(log, request, String.format("findProvidersByTvShowId : %s %s", tvShowId, isFlatrate));
        //TvShow tvShow = tvShowService.findById(tvShowId);
        Provider provider;
        try {
            provider = movieDbClient.searchTvShowProvider(movieDBApiKey, tvShowId);
        } catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                LogUtils.info(log, request, String.format("findProvidersByTvShowId: TV Show not found with id : %s", tvShowId));
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
