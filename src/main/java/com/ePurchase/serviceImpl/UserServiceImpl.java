package com.ePurchase.serviceImpl;

import com.ePurchase.Repository.UserRepository;
import com.ePurchase.domain.Product;
import com.ePurchase.domain.User;
import com.ePurchase.enums.Role;
import com.ePurchase.exception.BadClientDataException;
import com.ePurchase.service.UserService;
import com.ePurchase.utils.ItemLookupUtility;
import com.ePurchase.utils.ObjectMerger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by Get It on 9/8/2017.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<String> findIfUserAlreadyExist(User user) {

        List<String> verifyUserList = new ArrayList<>();
        User user1 = userRepository.findByMobile(user.getMobile());
        if (user1 != null) {
            verifyUserList.add("user with mobile " + user.getMobile() + " already exist.");
        }
        user1 = userRepository.findByEmail(user.getEmail());
        if (user1 != null) {
            verifyUserList.add("user with email " + user.getEmail() + " already exist.");
        }
        return verifyUserList;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public String checkForEmailReadOnlyAccess(User user) {

        User savedUser = userRepository.findOne(user.getId());
        if (savedUser != null) {
            if (savedUser.getEmail().equalsIgnoreCase(user.getEmail())) {
                return "Email can't be changed.";
            }
            return "";
        } else {
            return "user with id " + user.getId() + " does not exist";
        }
    }

    @Override
    public List<String> userValidation(User user) {


        List<String> validatonErrorList = new ArrayList<>();
        if (isEmpty(user.getEmail())) {
            validatonErrorList.add("email cannot be null or empty");
        }
        if (isEmpty(user.getMobile())) {
            validatonErrorList.add("mobile cannot be null or empty");
        }
        return validatonErrorList;
    }

    @Override
    public User updateUser(User user) {
        User savedUser = userRepository.findOne(user.getId());
        ObjectMerger.mergeClientEditableFields(user, savedUser);
        return userRepository.save(savedUser);
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findOne(userId);
        return user;
    }

    @Override
    public String setUserPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public String findIfUserIsAuthorizedToCreateUser(User user) {
        User loginedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ((loginedUser.getRole().equals(Role.SUPER_ADMIN) && !user.getRole().equals(Role.ADMIN)) ||
                (loginedUser.getRole().equals(Role.ADMIN) && !user.getRole().equals(Role.USER))) {
            return "you are not authorized for this action";
        }
        return "";
    }

    @Override
    public User addItemToCart(String userId, String itemId){

        if(isEmpty(userId)){
            throw new BadClientDataException("Invalid user id");
        }
        if(isEmpty(itemId)){
            throw new BadClientDataException("Invalid item id");
        }
        User user = userRepository.findOne(userId);
        boolean flag = checkIfItemIsAlreadyInCart(user.getItemId(),itemId);
            if(!flag){
                user.getItemId().add(itemId);
                userRepository.save(user);
            }
            else {
                throw new BadClientDataException("Item already in cart");
            }
            return user;
    }

    @Override
    public List<Product> getCartItems(String userId){
        String url = null;
        List<Product> productList = new ArrayList<>();
        if(isEmpty(userId)){
            throw new BadClientDataException("Invalid user id.");
        }
        else{
            User user = userRepository.findOne(userId);
            if(!user.getItemId().isEmpty()){
                for(String id : user.getItemId()){
                    url = ItemLookupUtility.getRequestUrlUsingAsinId(id);
                    productList.addAll(ItemLookupUtility.fetchTitle(url));
                }
            }
        }
        return productList;
    }

    @Override
    public List<Product> getProductsNavigation(String nodeId){
        String url = null;
        if(isEmpty(nodeId)){
            throw new BadClientDataException("Invalid node Id.");
        }
        url = ItemLookupUtility.getRequestUrlForSearchIndexId(nodeId);
        List<Product> productList = ItemLookupUtility.fetchTitle(url);
        return productList;

    }

    private boolean checkIfItemIsAlreadyInCart(List<String> itemIds, String itemId) {

        if(itemIds.contains(itemId))
            return true;
        else
            return false;
    }



}
