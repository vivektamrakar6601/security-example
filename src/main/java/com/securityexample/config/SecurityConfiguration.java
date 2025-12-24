package com.securityexample.config;

import com.securityexample.service.CustomerUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    String[] publicEndpoints ={
            "/api/v1/example/hello", "/api/v1/auth/signup"
            ,"/api/v1/admin/login","/api/v1/admin/welcome"
    };

    private final CustomerUserDetailsService customerUserDetailsService;


    // ✅ Constructor injection
    public SecurityConfiguration(CustomerUserDetailsService customerUserDetailsService) {
       this.customerUserDetailsService = customerUserDetailsService;

    }


    @Bean
    public SecurityFilterChain securityConfigurationnn(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(req ->
            req.requestMatchers(publicEndpoints).permitAll()
                    .requestMatchers("/api/v1/admin/hello").hasAnyRole("ADMIN","USER")
                    . anyRequest().authenticated()
                    )
                .httpBasic(Customizer.withDefaults());//for testing enables

        return http.build();
    }

    // ye config class h jo sbse phle run hokr iska object spring ioc m create
    // kr dega jo bhi bean detail hongi to ioc m chli jayengi
    // ab jb hm isko agay authservcie class m use krte h
    //tb ab ioc k pass se is pswrdencoder ki details object hoga
    // ishliye wo run kr payag bina error ke note: ioc passwrdenoder ka bean ni bna skta h isliye ye use kiya jata h
    @Bean
    public PasswordEncoder getEncoder() {

        return new BCryptPasswordEncoder();
    }

    //iska use verisylogin se related h ye bean creat krega authencatiin manager k liye
    //kykui iske liye ioc bean autu gentrat ni krega ye mnaula process krna hoga
    //new keywrd se ni bnega object isme ..
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authProvider(
            CustomerUserDetailsService customerUserDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customerUserDetailsService);
      provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
