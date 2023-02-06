package com.porpapps.moviesearchapi.service;

import com.porpapps.moviesearchapi.model.Quote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.porpapps.moviesearchapi.utils.FileUtils.randomLineFrom;

@RequiredArgsConstructor
@Service
public class QuoteService {
    //private final QuoteRepository quoteRepository;
    private final String QUOTES_FILEPATH = "/files/quotes.txt";

    public Quote findRandomQuote() {
        final var line = randomLineFrom(QUOTES_FILEPATH);
        if (line.isBlank()) {
            return new Quote("", "");
        }
        final var split = line.split(" - ");
        return new Quote(split[0], split[1]);
    }
}
