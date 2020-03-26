package com.elasticsearch.service;

import com.elasticsearch.bo.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Movie save(Movie Movie) throws Exception;
    void delete(Movie Movie) throws Exception;
    Optional<Movie> findOne(String id);
    Iterable<Movie> findAll();
    Page<Movie> findByName(String name, PageRequest pageRequest);
    List<Movie> findByName(String name);
}
