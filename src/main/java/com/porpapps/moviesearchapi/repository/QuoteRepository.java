package com.porpapps.moviesearchapi.repository;

import com.porpapps.moviesearchapi.model.Quote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QuoteRepository extends CrudRepository<Quote, Integer> {

    @Query("SELECT count(q) FROM Quote q")
    long count();
}
