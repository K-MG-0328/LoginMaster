package com.github.mingyu.loginmaster.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeContlroller {

    @GetMapping
    public String home() {
        System.out.println("home");
        return "home";
    }
}
