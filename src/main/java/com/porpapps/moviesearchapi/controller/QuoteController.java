package com.porpapps.moviesearchapi.controller;

import com.porpapps.moviesearchapi.model.Quote;
import com.porpapps.moviesearchapi.service.QuoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/quotes")
@RequiredArgsConstructor
@CrossOrigin(origins = {"${cross.origin.url}"})
public class QuoteController {
    private final QuoteService quoteService;

    @GetMapping("/random")
    public Quote findRandomQuote() {
        log.info("findRandomQuote");
        return quoteService.findRandomQuote();
    }
}
