package com.example.security_demo_v2.Config;


import com.example.security_demo_v2.Service.JpaUserDetailsService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig  {

    private RSAKey rsaKey;
    private final JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }


    /* 12/10/2023
     * Users are being authenticated through the database
     * Will implement bearer token authentication with username and password*/


    @Bean
    public AuthenticationManager authManager(JpaUserDetailsService jpaUserDetailsService){
        var authProvider = new DaoAuthenticationProvider(passwordEncoder());
        authProvider.setUserDetailsService(jpaUserDetailsService);
        return new ProviderManager(authProvider);
    }


    //Lambda DSL
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(auth -> {
                    //auth.requestMatchers("/").permitAll();
                    //auth.requestMatchers("/user").hasRole("USER");
                    auth.requestMatchers("/token").permitAll();
                    auth.anyRequest().authenticated();

                })
                .userDetailsService(jpaUserDetailsService)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                //.httpBasic(Customizer.withDefaults())
                .build();
 }

 @Bean
 public JWKSource<SecurityContext> jwkSource(){

        rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet) ;
 }

 @Bean
 JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks){
        return new NimbusJwtEncoder(jwks);
 }

 @Bean
 JwtDecoder jwtDecoder() throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
 }


@Bean
 CorsConfigurationSource corsConfigurationSource(){
     CorsConfiguration configuration = new CorsConfiguration();
     configuration.setAllowedOrigins(List.of("http://localhost:8084"));
     configuration.setAllowedMethods(List.of("GET","POST"));
     configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
     source.registerCorsConfiguration("/**", configuration);
     return source;
 }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
 }











 //    @Bean
//    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
//
//        http.authorizeHttpRequests()
//                .requestMatchers("/").permitAll()
//                .requestMatchers("/api/posts/**").hasRole("ADMIN")
//                .anyRequest().authenticated()
//                .and()
//                .csrf().disable()
//                .httpBasic();
//        return http.build();
//    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("password")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user,admin);
//    }


}
