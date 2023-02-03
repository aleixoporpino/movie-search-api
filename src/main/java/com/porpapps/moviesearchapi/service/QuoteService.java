package com.porpapps.moviesearchapi.service;

import com.porpapps.moviesearchapi.model.Quote;
import com.porpapps.moviesearchapi.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;

    public Quote findRandomQuote() {
        final var random = new Random();
        final var quotesTotal = (int) quoteRepository.count();
        return quoteRepository.findById(random.nextInt(quotesTotal)).orElse(null);
    }
}
