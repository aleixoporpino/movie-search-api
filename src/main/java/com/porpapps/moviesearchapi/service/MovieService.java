package com.porpapps.moviesearchapi.service;

import com.porpapps.moviesearchapi.model.Movie;
import com.porpapps.moviesearchapi.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public List<Movie> findMoviesByName(String name) {
        return movieRepository.findAllByOriginalTitleContainingIgnoreCaseOrderByOriginalTitle(name);
    }

    public Movie findById(Integer id) {
        return movieRepository.findById(id).orElse(new Movie(id, ""));
    }
}
