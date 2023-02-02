package com.porpapps.moviesearchapi.service;

import com.porpapps.moviesearchapi.model.Quote;
import com.porpapps.moviesearchapi.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;

    public Quote findById(Integer id) {
        return quoteRepository.findById(id).orElse(null);
    }
}
