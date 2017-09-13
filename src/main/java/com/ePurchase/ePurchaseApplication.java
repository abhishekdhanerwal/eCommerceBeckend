package com.ePurchase;

import com.ePurchase.Repository.UserRepository;
import com.ePurchase.domain.User;
import com.ePurchase.enums.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by Get It on 9/8/2017.
 */
@SpringBootApplication
@EnableWebSecurity
@RestController
public class ePurchaseApplication extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic()
                .and()
                .requestMatchers()
                .antMatchers("/login")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @RequestMapping("/login")
    public Principal user(Principal user) {
        return user;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String args[]){SpringApplication.run(ePurchaseApplication.class,args);}

    @Bean
    CommandLineRunner init(UserRepository userRepository){
        return (evt)->{
            userRepository.deleteAll();
            User user = new User("Vijender","vijender9423@gmail.com","secret","9958046526", Role.SUPER_ADMIN);
            userRepository.save(user);
            User user1 = new User("Abhishek","abhishek@gmail.com","secret","9911866043",Role.SUPER_ADMIN);
            userRepository.save(user1);
        };

    }
}

