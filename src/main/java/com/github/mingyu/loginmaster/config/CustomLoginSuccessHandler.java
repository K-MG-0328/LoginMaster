package com.github.mingyu.loginmaster.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        //유저 정보를 가지고 DB에 저장하는 로직 구성
        String username = authentication.getName();

        request.getSession().setAttribute("loginUser", username);
        request.getSession().setAttribute("loginTime", System.currentTimeMillis());

        response.sendRedirect("/home");
    }
}
