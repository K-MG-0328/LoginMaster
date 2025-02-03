package com.github.mingyu.loginmaster.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // 1. 인증된 사용자 정보 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 2. 사용자 정보를 통해 DB 등에서 추가 정보 조회 가능
        //    예: userService.findByEmail(...) 등

        // 3. 세션에 직접 저장 이 과정을 해주지 않더라도 SpringSecurity는 로그인 성공 시점에 자동으로 세션에 저장함.
        System.out.println(
                request.getSession().getAttribute("SPRING_SECURITY_CONTEXT")
        );
        
        // 4. 이후 처리 (리다이렉트 등)
        response.sendRedirect("/home");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
