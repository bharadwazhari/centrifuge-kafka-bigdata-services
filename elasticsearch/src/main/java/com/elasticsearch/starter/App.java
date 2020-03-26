package com.elasticsearch.starter;

import com.elasticsearch.bo.Movie;
import com.elasticsearch.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.Calendar;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@SpringBootApplication
@ComponentScan(basePackages = "com.elasticsearch")
public class App implements CommandLineRunner {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private MovieService mservice;

    public static void main(String... args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("**************** START OF findAll() method ***********************");
        mservice.findAll().forEach(e -> System.out.println(e));
        System.out.println("**************** End OF findAll() method ***********************");

        System.out.println("**************** START OF Insert() method ***********************");
        Movie movie = new Movie();
        movie.setName("Example Movie millinium");
        movie.setActor_count(2000);
        movie.setDate(Calendar.getInstance().getTime());
        Movie sobj = mservice.save(movie);
        Optional<Movie> obj = mservice.findOne(sobj.getId());
        System.out.println(obj);
        System.out.println("**************** End OF Insert() method ***********************");

        System.out.println("**************** Start OF Update() method ***********************");
        sobj.setName("HARI");
        mservice.save(sobj);
        mservice.findAll().forEach(e -> System.out.println(e));
        System.out.println("**************** End OF Update() method ***********************");

        System.out.println("**************** Start OF Delete() method ***********************");
        mservice.findAll().forEach(e -> System.out.println(e));
        mservice.delete(sobj);
        System.out.println("DELETED");
        mservice.findAll().forEach(e -> System.out.println(e));
        System.out.println("**************** End OF Delete() method ***********************");

        System.out.println("**************** Start OF Page() method ***********************");
        Page<Movie> pages = mservice.findByName("Example Movie Two", PageRequest.of(0,3));
        pages.forEach(e -> System.out.println(e));
        System.out.println("**************** End OF Page() method ***********************");

        System.out.println("**************** Start OF Native Query Build ***********************");
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("name", "Example Movie"))
                .build();
        template.queryForList(searchQuery, Movie.class).forEach(e -> System.out.println(e));
        System.out.println("**************** End OF Native Query Build ***********************");

        /*System.out.println("**************** Start OF COUNTS ***********************");
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchQuery searchQuery1  = queryBuilder.
        System.out.println("No. Of Records :"+template.count(searchQuery1));
        System.out.println("**************** Start OF COUNTS ***********************");*/
    }



}
