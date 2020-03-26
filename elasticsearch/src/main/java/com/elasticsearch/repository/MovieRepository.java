package com.elasticsearch.repository;

import com.elasticsearch.bo.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MovieRepository extends ElasticsearchRepository<Movie, String> {
    Page<Movie> findByName(String name, PageRequest pageRequest);
    List<Movie> findByName(String name);
}
