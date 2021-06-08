package com.schooldevops.security_sample.configs;

import com.schooldevops.security_sample.filters.MySecurityFilter;
import com.schooldevops.security_sample.security.MyAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.password.encoder}")
    String defaultPasswordEncoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MyAuthenticationProvider authenticationProvider;

//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
////        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        //  AuthorizationManager 를 생성한다. 여기서는 메모리에 적재해두고 사용할 것이기 때문에 InMemoryUserDetailManager를 이용했다.
//        InMemoryUserDetailsManager userDetailService = new InMemoryUserDetailsManager();
//
//        //  User 를 지정한다.
//        //  username: custom_user, password: custom_user_123 으로 지정했다.
//        UserDetails user = User.withUsername("custom_user").password(passwordEncoder.encode("custom_user_123")).authorities("read").build();
//        userDetailService.createUser(user);
//
//
//        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //  HttpSecurity 는 Basic Authentication 을 이용하겠다고 지정한다.
        http.httpBasic();  // Basic Auth 용
//        http.formLogin();   // Form Login 용

        //  인증된 요청만 통과하도록 지정한다.
//        http.authorizeRequests().anyRequest().authenticated();
        http.authorizeRequests().antMatchers("/users").authenticated().anyRequest().permitAll();
//        http.authorizeRequests().antMatchers("/users").authenticated().anyRequest().denyAll();

        // 필터를 등록한다.
        http.addFilterBefore(new MySecurityFilter(), BasicAuthenticationFilter.class);
//        http.addFilter(Filter filter);
//        http.addFilterAfter(Filter filter, Class<? extends Filter> clazz);
//        http.addFilterAt(Filter filter, Class<? extends Filter> clazz);
    }

//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        System.out.println("------ Encoder: " + defaultPasswordEncoder);
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());

        DelegatingPasswordEncoder passworEncoder = new DelegatingPasswordEncoder(defaultPasswordEncoder, encoders);

        return passworEncoder;
    }
}
