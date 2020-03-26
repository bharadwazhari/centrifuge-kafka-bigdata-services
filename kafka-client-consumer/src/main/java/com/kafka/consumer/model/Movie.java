package com.kafka.consumer.model;

import lombok.Data;

import java.util.Date;

@Data
public class Movie {
    private String id;
    private String name;
    private int actor_count;
    private Date date;
}
