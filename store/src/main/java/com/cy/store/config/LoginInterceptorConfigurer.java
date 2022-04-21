package com.cy.store.config;

import com.cy.store.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration// Load the current interceptor and register it
public class LoginInterceptorConfigurer implements WebMvcConfigurer {
    /** Interceptor configuration */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Create an interceptor object
        HandlerInterceptor interceptor = new LoginInterceptor();

        // White list
        List<String> patterns = new ArrayList<String>();
        patterns.add("/bootstrap3/**");
        patterns.add("/css/**");
        patterns.add("/images/**");
        patterns.add("/js/**");
        patterns.add("/web/register.html");
        patterns.add("/web/login.html");
        patterns.add("/web/index.html");
        patterns.add("/web/product.html");
        patterns.add("/users/reg");
        patterns.add("/users/login");
        patterns.add("/districts/**");
        patterns.add("/products/**");
        patterns.add("/user/login");
        patterns.add("/districts/**");
        // Add interceptors through the registration tool
        registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns(patterns);


    }
}
