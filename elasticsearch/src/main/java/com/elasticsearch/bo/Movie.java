package com.elasticsearch.bo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Document(indexName = "my_movies", type = "_doc")
public class Movie {
    @Id
    private String id;
    private String name;
    private int actor_count;
    private Date date;
}
