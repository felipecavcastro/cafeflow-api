package com.cafeflow.config;

import com.cafeflow.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Rotas públicas (Cadastro e Login abertos)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/users/register", "/auth/login").permitAll()
                        // Rotas estritamente protegidas: Só o DONO (ADMIN) vê o faturamento bruto
                        .requestMatchers("/bookings/revenue").hasRole("ADMIN")
                        // Qualquer outra rota (incluindo o novo /bookings/history) exige apenas estar logado
                        .anyRequest().authenticated()
                )
                // AJUSTE AQUI: Dizemos ao Spring para rodar o nosso filtro ANTES do filtro padrão de usuário/senha
                .addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
