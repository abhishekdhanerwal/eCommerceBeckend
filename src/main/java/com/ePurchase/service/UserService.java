package com.ePurchase.service;

import com.ePurchase.domain.User;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Get It on 9/8/2017.
 */
@Document
public interface UserService {
    List<String> findIfUserAlreadyExist(User user);

    User createUser(User user);

    String checkForEmailReadOnlyAccess(User user);

    List<String> userValidation(User user);

    User updateUser(User user);

    User getUser(String userId);
}
