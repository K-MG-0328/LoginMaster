package com.github.mingyu.loginmaster.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;

public class CustomAuthenticationFilter  extends UsernamePasswordAuthenticationFilter {
    private final SecurityContextRepository securityContextRepository;

    public CustomAuthenticationFilter(HttpSecurity http) {
        SecurityContextRepository repository = getSecurityContextRepository(http);
        this.securityContextRepository = repository;
        setSecurityContextRepository(repository);
    }

    private SecurityContextRepository getSecurityContextRepository(HttpSecurity http) {
        SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
        if (securityContextRepository == null) {
            securityContextRepository =
                    new DelegatingSecurityContextRepository(new HttpSessionSecurityContextRepository(), new RequestAttributeSecurityContextRepository());
        }
        return securityContextRepository;
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
        this.setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // SecurityContext 생성 및 저장
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authResult);

        //SecurityContextHolder, SecirityContextRepository에 저장
        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, request, response);

        // 세션에 SecurityContext 저장 (인증 영속성 유지)
        HttpSession session = request.getSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

        // 성공 핸들러 실행
        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
