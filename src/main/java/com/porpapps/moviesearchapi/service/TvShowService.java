package com.porpapps.moviesearchapi.service;

import com.porpapps.moviesearchapi.model.TvShow;
import com.porpapps.moviesearchapi.repository.TvShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TvShowService {
    private final TvShowRepository tvShowRepository;

    public List<TvShow> findMoviesByName(String name) {
        return tvShowRepository.findAllByOriginalNameContainingIgnoreCaseOrderByOriginalName(name);
    }

    public TvShow findById(Integer id) {
        return tvShowRepository.findById(id).orElse(new TvShow(id, ""));
    }
}
