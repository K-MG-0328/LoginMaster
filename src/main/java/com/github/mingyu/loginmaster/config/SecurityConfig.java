package com.github.mingyu.loginmaster.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticatonManager = authenticationManager(authenticationConfiguration);

        http
                .sessionManagement(session -> session // 필요할 때 세션을 생성하여 인증 유지
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .addFilterBefore(customAuthenticationFilter(http, authenticatonManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(customAuthenticationProvider)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login/*", "/logout", "/register", "/customLogin").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                //requireExplicitSave를 false로 하게되면 SecurityContextPersistenceFilter를 타게되고 세션에 자동으로 저장이된다. 스프링 5버전과 동일하게 적용됨.
                //.securityContext(securityContext -> securityContext.requireExplicitSave(false))
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );
        return http.build();
    }

    public CustomAuthenticationFilter customAuthenticationFilter(HttpSecurity http, AuthenticationManager authenticationManager) {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(http);
        customAuthenticationFilter.setAuthenticationManager(authenticationManager);
        customAuthenticationFilter.setFilterProcessesUrl("/customLogin");
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler);

        return customAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        //authenticationManager 구현체(ProviderMananger)
        return authenticationConfiguration.getAuthenticationManager();
    }
}
