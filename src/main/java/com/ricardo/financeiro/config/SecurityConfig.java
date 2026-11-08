package com.ricardo.financeiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ricardo.financeiro.security.AppUserDetailsService;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = AppUserDetailsService.class)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

//    @Autowired
//    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
            		 .requestMatchers(
            	                "/layout/**",
            	                "/images/**",
            	                "/css/**",
            	                "/js/**",
            	                "/fonts/**"
            	            ).permitAll()
            		 
                .requestMatchers("/cidades/novo").hasRole("CADASTRAR_CIDADE")
                .requestMatchers("/categorias/novo").hasRole("CADASTRAR_CATEGORIA")
                .requestMatchers(HttpMethod.POST, "/categorias").hasRole("CADASTRAR_CATEGORIA")
                .requestMatchers("/clientes/novo").hasRole("CADASTRAR_CLIENTE")
                .requestMatchers("/lancamentos/novo").hasRole("CADASTRAR_LANCAMENTO")
                .requestMatchers("/livros/novo").hasRole("CADASTRAR_LIVRO")
                // .requestMatchers("/usuarios/**").hasRole("CADASTRAR_USUARIO")
                .requestMatchers("/usuarios/status").hasRole("CADASTRAR_LANCAMENTO")
                .requestMatchers("/usuarios/novo").hasRole("CADASTRAR_USUARIO")
                .requestMatchers("/grupos").hasRole("ASSOCIAR_GRUPO_PERMISSAO")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .permitAll()
            )

            .exceptionHandling(exception -> exception
                .accessDeniedPage("/403")
            )

            .sessionManagement(session -> session
                .invalidSessionUrl("/login")
            );

        return http.build();
    }
    
	/*
	 * @Bean public WebSecurityCustomizer webSecurityCustomizer() { return web ->
	 * web.ignoring() .requestMatchers("/layout/**")
	 * .requestMatchers("/images/logo_login.svg") .requestMatchers("/images/**")
	 * .requestMatchers("/css/**") .requestMatchers("/js/**")
	 * .requestMatchers("/fonts/**"); }
	 */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}