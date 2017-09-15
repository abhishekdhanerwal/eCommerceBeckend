package com.ePurchase.service;


import com.ePurchase.domain.User;
import com.ePurchase.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public UserDetailService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmailIgnoreCase(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email=%s was not found", email));
        } else {
            return user;
        }

    }
}
