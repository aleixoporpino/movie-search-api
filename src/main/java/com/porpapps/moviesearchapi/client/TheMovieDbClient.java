package com.porpapps.moviesearchapi.client;

import com.porpapps.moviesearchapi.client.model.Provider;
import com.porpapps.moviesearchapi.client.model.QueryResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "themoviedb", url = "https://api.themoviedb.org/3/")
public interface TheMovieDbClient {

    @GetMapping(value = "/search/movie?api_key={apiKey}")
    QueryResult searchMovieByName(@PathVariable(name = "apiKey") String apiKey, @RequestParam(name = "query") String query);

    @GetMapping(value = "/movie/{movieId}/watch/providers?api_key={apiKey}")
    Provider searchMovieProvider(@PathVariable(name = "apiKey") String apiKey, @PathVariable(name = "movieId") Integer movieId);

    @GetMapping(value = "/search/tv?api_key={apiKey}")
    QueryResult searchTvShowByName(@PathVariable(name = "apiKey") String apiKey, @RequestParam(name = "query") String query);

    @GetMapping(value = "/tv/{tvShowId}/watch/providers?api_key={apiKey}")
    Provider searchTvShowProvider(@PathVariable(name = "apiKey") String apiKey, @PathVariable(name = "tvShowId") Integer tvShowId);
}
