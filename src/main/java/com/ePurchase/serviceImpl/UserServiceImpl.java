package com.ePurchase.serviceImpl;

import com.ePurchase.Repository.UserRepository;
import com.ePurchase.domain.User;
import com.ePurchase.service.UserService;
import com.ePurchase.utils.ObjectMerger;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<String> findIfUserAlreadyExist(User user ){

            List<String> verifyUserList = new ArrayList<>();
            User user1 = userRepository.findByMobile(user.getMobile());
            verifyUserList.add("user with mobile "+user.getMobile()+" already exist.");
            user1 = userRepository.findByEmail(user.getEmail());
            verifyUserList.add("user with email "+user.getEmail()+" already exist.");
        return verifyUserList;
    }

    @Override
    public User createUser(User user){
        return userRepository.save(user);
    }

    @Override
    public String checkForEmailReadOnlyAccess(User user){

            User savedUser = userRepository.findOne(user.getId());
            if(savedUser!=null) {
                if (savedUser.getEmail().equalsIgnoreCase(user.getEmail())) {
                    return "Email can't be changed.";
                }
                return "";
            }else{
                return "user with id "+user.getId()+" does not exist";
            }
    }

    @Override
    public List<String> userValidation(User user){


        List<String> validatonErrorList = new ArrayList<>();
        if(isEmpty(user.getEmail())){
            validatonErrorList.add("email cannot be null or empty");
        }
        if(isEmpty(user.getMobile())){
            validatonErrorList.add("mobile cannot be null or empty");
        }
        return validatonErrorList;
    }

    @Override
    public User updateUser(User user){
        User savedUser = userRepository.findOne(user.getId());
        ObjectMerger.mergeClientEditableFields(user,savedUser);
        return userRepository.save(savedUser);
    }

    @Override
    public User getUser(String userId){
        User user = userRepository.findOne(userId);
        return user;
    }
}
