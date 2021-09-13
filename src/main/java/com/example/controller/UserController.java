package com.example.controller;

import com.example.model.User;
import com.example.request.SearchParams;
import com.example.service.UserService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Get("/users{?params*}")
    List<User> getUsers(LinkedHashMap<String, String> params) {
        SearchParams searchParams = SearchParams.build(params);
        System.out.println(searchParams);
        return userService.getUsers(searchParams);
    }

    @Get("/save")
    void saveData() {
        userService.save();
    }
}
