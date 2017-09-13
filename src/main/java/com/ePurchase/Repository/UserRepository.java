package com.ePurchase.Repository;

import com.ePurchase.domain.User;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Get It on 9/8/2017.
 */
public interface UserRepository extends MongoRepository<User,String> {
    User findByMobile(String mobile);

    User findByEmail(String email);

    User findByEmailIgnoreCase(String email);
}
