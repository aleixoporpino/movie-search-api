package com.porpapps.moviesearchapi.controller;

import com.porpapps.moviesearchapi.client.TheMovieDbClient;
import com.porpapps.moviesearchapi.client.model.Provider;
import com.porpapps.moviesearchapi.client.model.QueryResult;
import com.porpapps.moviesearchapi.repository.MovieRepository;
import com.porpapps.moviesearchapi.service.MovieService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/movies")
@RequiredArgsConstructor
@CrossOrigin(origins = {"${cross.origin.url}"})
public class MovieController {

    private static final Logger LOG = LoggerFactory.getLogger(MovieController.class);

    private final MovieService movieService;
    private final TheMovieDbClient movieDbClient;
    @Value("${moviedb.api.key}")
    private String movieDBApiKey;

    @GetMapping("/name/{movieName}")
    public QueryResult findMoviesByName(@PathVariable String movieName) {
        LOG.info("findMoviesByName : {}", movieName);
        return movieDbClient.searchMovieByName(movieDBApiKey, movieName);
    }

    @GetMapping("/{movieId}")
    public Provider findProvidersByMovieId(@PathVariable Integer movieId,
                                           @RequestParam(name = "showStreamingOnly", required = false) boolean isFlatrate) {
        LOG.info("findProvidersByMovieId : {} {}", movieId, isFlatrate);
        //Movie movie = movieService.findById(movieId);
        Provider provider;
        try {
            provider = movieDbClient.searchMovieProvider(movieDBApiKey, movieId);
        } catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                LOG.info("findProvidersByTvShowId: TV Show not found with id: {}", movieId);
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
