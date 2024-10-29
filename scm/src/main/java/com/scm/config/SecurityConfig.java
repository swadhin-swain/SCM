package com.scm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.scm.helper.Message;
import com.scm.helper.MessegeType;
import com.scm.services.impl.SecurityCustomUserDetailsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailsService userDetailsService;

    @Autowired
    private OauthAuthenticationSucessHandler handler;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf->csrf.disable())
        .cors(cors->cors.disable())
        .authorizeHttpRequests(auth->{
            auth.requestMatchers("/user/**").authenticated()
            .anyRequest().permitAll();
        })
        // form default login
        // agar hame kuch change karna hua to hum yaha ayange: form login se related
        .formLogin(formLogin->{
            
            // 
            formLogin
            .loginPage("/login")
            .loginProcessingUrl("/authenticate")
            .defaultSuccessUrl("/user/profile", true)

            // .failureForwardUrl("/login?error = true")
            .usernameParameter("email")
            .passwordParameter("password");
            

            // when authentication is failed
            formLogin.failureHandler(new AuthenticationFailureHandler() {

                @Override
                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException exception) throws IOException, ServletException {
                   // this is anonymous inner class

                   if (exception instanceof DisabledException) {
                    
                    // user disabled
                    HttpSession session = request.getSession();
                    session.setAttribute("message", Message.builder().
                    content("User is disabled, Email with verification link is send on yuor email id !!")
                                     .type(MessegeType.red).build());

                    
                     response.sendRedirect("/login");
                   }
                   else {
                    response.sendRedirect("/login?error=true");
                   }

                   
                }

            });

            
        });

        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        // oauth configuration
        // configuration for google

        httpSecurity.oauth2Login(oauth-> {
            oauth.loginPage("/login")
            .successHandler(handler);
        });

        return httpSecurity.build();
    }
}
