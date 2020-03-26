package com.elasticsearch.bo;

import com.elasticsearch.bo.Part;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Product {
    @Id
    private String id;
    private List<Part> parts;
    private String name;
    private String color;
}
