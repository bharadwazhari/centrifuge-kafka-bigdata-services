package com.kafka.producer.model;

import lombok.Data;

import java.util.Date;

@Data
public class Movie {
    private String id;
    private String name;
    private int actor_count;
    private Date date;
}
