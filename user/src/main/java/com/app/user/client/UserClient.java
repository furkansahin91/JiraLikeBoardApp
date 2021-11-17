package com.app.user.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.app.user.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@AllArgsConstructor
public class UserClient {
    CustomerService userService;
    ObjectMapper mapper;

    RestTemplate restTemplate;

    @Autowired
    public UserClient(CustomerService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }


    @PostConstruct
    public void init() {
        mapper = new ObjectMapper();
    }


    public void start() {


    }

}
