package com.elasticsearch.resource;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/")
public class HellowWorld {

    @RequestMapping(method= RequestMethod.GET)
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
