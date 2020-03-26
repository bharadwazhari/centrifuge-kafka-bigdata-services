package com.elasticsearch.service;

import com.elasticsearch.bo.Movie;
import com.elasticsearch.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieRepository repo;

    @Override
    public Movie save(Movie movie) throws Exception {
        Movie rmovie =  repo.save(movie);
        return rmovie;
    }

    @Override
    public void delete(Movie movie) throws Exception {
        repo.delete(movie);
    }

    @Override
    public Optional<Movie> findOne(String id) {
        return repo.findById(id);
    }

    @Override
    public Iterable<Movie> findAll() {
        return repo.findAll();
    }

    @Override
    public Page<Movie> findByName(String name, PageRequest pageRequest) {
        return repo.findByName(name, pageRequest);
    }

    @Override
    public List<Movie> findByName(String name) {
        return repo.findByName(name);
    }
}
