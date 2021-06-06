package com.schooldevops.security_sample.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //  Authorization Provider 에서 사용할 PasswordEncoder 생성
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //  AuthorizationManager 를 생성한다. 여기서는 메모리에 적재해두고 사용할 것이기 때문에 InMemoryUserDetailManager를 이용했다.
        InMemoryUserDetailsManager userDetailService = new InMemoryUserDetailsManager();

        //  User 를 지정한다.
        //  username: custom_user, password: custom_user_123 으로 지정했다.
        UserDetails user = User.withUsername("custom_user").password(passwordEncoder.encode("custom_user_123")).authorities("read").build();
        userDetailService.createUser(user);


        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //  HttpSecurity 는 Basic Authentication 을 이용하겠다고 지정한다.
        http.httpBasic();

        //  인증된 요청만 통과하도록 지정한다.
        http.authorizeRequests().anyRequest().authenticated();

    }
}
