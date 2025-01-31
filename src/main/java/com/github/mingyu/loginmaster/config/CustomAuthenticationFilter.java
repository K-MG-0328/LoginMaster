package com.github.mingyu.loginmaster.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter  extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final CustomLoginSuccessHandler successHandler;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, CustomLoginSuccessHandler successHandler) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/customLogin"); //customLogin URL로 들어온 요청은 해당 필터에서 처리
        this.successHandler = successHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        HttpSession session = request.getSession();
        String username = session.getAttribute("username") != null ? session.getAttribute("username").toString().trim() : "";
        String password = session.getAttribute("password") != null ? session.getAttribute("password").toString() : "";

        if (username.isEmpty() || password.isEmpty()) {
            throw new AuthenticationException("Invalid session credentials") {};
        }

        UsernamePasswordAuthenticationToken authRequest =  new UsernamePasswordAuthenticationToken(username, password);

        /* setDetails()는 로그인 요청의 추가 정보를 Authentication Token에 저장하는 메서드
           - 세션 정보(Session ID)
           - 원격 IP 주소(Remote Address)
           - 기타 인증 관련 요청 정보 */
        setDetails(request, authRequest);

        return authenticationManager.authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }
}
