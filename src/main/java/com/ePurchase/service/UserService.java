package com.ePurchase.service;

import com.ePurchase.domain.Product;
import com.ePurchase.domain.User;

import java.util.List;

/**
 * Created by Get It on 9/8/2017.
 */

public interface UserService {
    List<String> findIfUserAlreadyExist(User user);

    User createUser(User user);

    String checkForEmailReadOnlyAccess(User user);

    List<String> userValidation(User user);

    User updateUser(User user);

    User getUser(String userId);

    String setUserPassword(String password);

    String findIfUserIsAuthorizedToCreateUser(User user);

    User addItemToCart(String userId, String itemId);

    List<Product> getCartItems(String userId);

    List<Product> getProductsNavigation(String nodeId, String page, String searchIndex);
}
