package com.github.mingyu.loginmaster.controller;

import com.github.mingyu.loginmaster.dto.RequestDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping("/redirect")
    public String login(@ModelAttribute RequestDTO requestDTO, HttpSession session) {
        session.setAttribute("username", requestDTO.getEmail());
        session.setAttribute("password", requestDTO.getPassword());
        return "redirect:/customLogin";
    }

}
