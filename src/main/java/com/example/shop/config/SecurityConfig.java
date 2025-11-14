package com.example.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //  CSRF включён, токен выдаётся в cookie XSRF-TOKEN
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        // Для удобства разработки и Postman:
                        // на /api/auth/** и /api/orders/** CSRF не проверяем
                        .ignoringRequestMatchers("/api/auth/**", "/api/orders/**", "/api/order-items/**")
                )
                .authorizeHttpRequests(auth -> auth

                        // Регистрация – доступна всем (но без CSRF)
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()

                        // Публичный каталог – GET /products, /categories
                        .requestMatchers(HttpMethod.GET, "/api/products/**", "/api/categories/**").permitAll()

                        // Категории и товары – только ADMIN (и здесь CSRF включён!)
                        .requestMatchers("/api/products/**", "/api/categories/**").hasRole("ADMIN")

                        // Клиенты – только ADMIN
                        .requestMatchers("/api/customers/**").hasRole("ADMIN")

                        // Заказы и позиции – USER или ADMIN
                        .requestMatchers("/api/orders/**", "/api/order-items/**").hasAnyRole("USER", "ADMIN")

                        // Остальное – просто требует авторизации
                        .anyRequest().authenticated()
                )
                // Basic Auth: логин/пароль из БД через UserDetailsService
                .httpBasic(basic -> {});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
