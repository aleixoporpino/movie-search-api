package com.porpapps.moviesearchapi.repository;

import com.porpapps.moviesearchapi.model.Quote;
import org.springframework.data.repository.CrudRepository;

public interface QuoteRepository extends CrudRepository<Quote, Integer> {
}
