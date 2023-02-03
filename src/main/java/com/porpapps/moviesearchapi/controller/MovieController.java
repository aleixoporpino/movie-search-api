package com.porpapps.moviesearchapi.controller;

import com.porpapps.moviesearchapi.client.TheMovieDbClient;
import com.porpapps.moviesearchapi.client.model.Provider;
import com.porpapps.moviesearchapi.client.model.QueryResult;
import com.porpapps.moviesearchapi.service.MovieService;
import com.porpapps.moviesearchapi.utils.LogUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/movies")
@RequiredArgsConstructor
@CrossOrigin(origins = {"${cross.origin.url}"})
public class MovieController {
    private final MovieService movieService;
    private final TheMovieDbClient movieDbClient;
    @Value("${moviedb.api.key}")
    private String movieDBApiKey;

    @GetMapping("/name/{movieName}")
    public QueryResult findMoviesByName(@PathVariable String movieName, HttpServletRequest request) {
        LogUtils.info(log, request, String.format("findMoviesByName : %s", movieName));
        return movieDbClient.searchMovieByName(movieDBApiKey, movieName);
    }

    @GetMapping("/{movieId}")
    public Provider findProvidersByMovieId(@PathVariable Integer movieId,
                                           @RequestParam(name = "showStreamingOnly", required = false) boolean isFlatrate,
                                           HttpServletRequest request) {
        LogUtils.info(log, request, String.format("findProvidersByMovieId : %s %s", movieId, isFlatrate));
        //Movie movie = movieService.findById(movieId);
        Provider provider;
        try {
            provider = movieDbClient.searchMovieProvider(movieDBApiKey, movieId);
        } catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                LogUtils.info(log, request, String.format("findProvidersByMovieId: TV Show not found with id: %s", movieId));
                return new Provider();
            }
            throw e;
        }
        provider.setLink("https://www.themoviedb.org/movie/" + movieId);
        //provider.setName(movie.getOriginalTitle());
        if (isFlatrate) {
            final var flatMovieProviders = new Provider();
            flatMovieProviders.setId(provider.getId());
            flatMovieProviders.setLink("https://www.themoviedb.org/movie/" + movieId);
            //flatMovieProviders.setName(movie.getOriginalTitle());
            for (final var result : provider.getResults().entrySet()) {
                if (result.getValue().getFlatrate() != null) {
                    flatMovieProviders.getResults().put(result.getKey(), result.getValue());
                }
            }
            return flatMovieProviders;
        }
        return provider;
    }
}
