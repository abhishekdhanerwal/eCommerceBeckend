package com.ePurchase.service;

import com.ePurchase.domain.Product;
import com.ePurchase.domain.User;

import java.util.List;
import java.util.Map;

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

    List<Product> getAllProductsForNode(String nodeId, String pageNo, String sort, String searchIndex);

    Map<String, Object> getAllVariationProducts(String asinId);
}
