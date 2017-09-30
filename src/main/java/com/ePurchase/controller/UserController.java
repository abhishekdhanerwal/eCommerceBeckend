package com.ePurchase.controller;

import com.ePurchase.domain.Product;
import com.ePurchase.domain.User;
import com.ePurchase.exception.BadClientDataException;
import com.ePurchase.service.UserService;
import com.ePurchase.utils.ItemLookupUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by Get It on 9/8/2017.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private RestTemplate restTemplate;
    private UserService userService;
    private PasswordEncoder passwordEncoder;



    @Autowired
    public UserController(RestTemplate restTemplate, UserService userService, PasswordEncoder passwordEncoder) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user) {

        if (user != null) {
            if (isEmpty(user.getEmail())) {
                throw new BadClientDataException("Invalid user email");
            }
            if (isEmpty(user.getMobile())) {
                throw new BadClientDataException("Invalid user mobile");
            }
            String authorizedUserErrorMsg = userService.findIfUserIsAuthorizedToCreateUser(user);
            if (isEmpty(authorizedUserErrorMsg)) {
                List<String> userAlreadyExistFlag = userService.findIfUserAlreadyExist(user);
                if (userAlreadyExistFlag.isEmpty()) {
                    User savedUser = userService.createUser(user);
                    return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<List<String>>(userAlreadyExistFlag, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<String>(authorizedUserErrorMsg, HttpStatus.OK);
            }
        } else {
            throw new BadClientDataException("Invalid user details");
        }
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        if (user != null) {
            List<String> validationErrorsList = userService.userValidation(user);
            if (validationErrorsList.isEmpty()) {
                String emailReadOnlyAccessError = userService.checkForEmailReadOnlyAccess(user);
                if (isEmpty(emailReadOnlyAccessError)) {
                    throw new BadClientDataException(emailReadOnlyAccessError);
                } else {
                    User updateduser = userService.updateUser(user);
                    return new ResponseEntity<User>(updateduser, HttpStatus.CREATED);
                }
            } else {
                throw new BadClientDataException(validationErrorsList.stream().toArray(String[]::new));
            }
        } else {
            throw new BadClientDataException("user cannot be null");
        }
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwords,
                                            UsernamePasswordAuthenticationToken principal) {

        String oldPassword = passwords.get("oldPassword");
        String newPassword = passwords.get("newPassword");

        User currentUser = (User) principal.getPrincipal();
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new BadClientDataException("Current password is incorrect.");
        }

        if (oldPassword.equals(newPassword)) {
            throw new BadClientDataException("Old password and New password can not be same.");
        } else {
            currentUser.setPasswordHash(new BCryptPasswordEncoder().encode(newPassword));
            currentUser = userService.createUser(currentUser);
            return new ResponseEntity<User>(currentUser, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody User user) {

        String passwordHash = userService.setUserPassword(user.getPassword());
        user.setPasswordHash(passwordHash);
        user = userService.createUser(user);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(value="/getNavigationBoard" , method = RequestMethod.GET)
    public List<Product> getProductsNavigation(){
        ItemLookupUtility itemLookupUtility = new ItemLookupUtility();
        String requestUrl = itemLookupUtility.getRequestUrl("976390031");
        List<Product> products = itemLookupUtility.fetchTitle(requestUrl);
        return products;

    }
}

