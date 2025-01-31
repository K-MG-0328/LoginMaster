package com.github.mingyu.loginmaster.controller;

import com.github.mingyu.loginmaster.dto.RequestDTO;
import com.github.mingyu.loginmaster.entity.User;
import com.github.mingyu.loginmaster.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;


@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterConteroller {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String register() {
        return "register";
    }

    @PostMapping
    public String register(@ModelAttribute RequestDTO requestDTO) {
        User user = new User(
                requestDTO.getEmail()
                ,passwordEncoder.encode(requestDTO.getPassword())
                , LocalDateTime.now()
                , LocalDateTime.now()
        );
        userRepository.save(user);
        return "redirect:/login";
    }
}
