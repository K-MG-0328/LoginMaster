package com.github.mingyu.loginmaster.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        //권한 받지 않은 url 접속 테스트
        return "test";
    }
}
