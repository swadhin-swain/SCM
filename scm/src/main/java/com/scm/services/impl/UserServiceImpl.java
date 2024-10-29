package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repository.UserRepo;
import com.scm.services.EmailService;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EmailService emailService;

    @Autowired
    private  Helper helper;

    @Override
    public User saveUser(User user) {
        // save the user id
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // set the user role
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        logger.info(user.getProvider().toString());

         String emailToken = UUID.randomUUID().toString();

         user.setEmailToken(emailToken);

         User savedUser = userRepo.save(user);

         String emailLink = helper.getLinkForEmailVerification(emailToken);

         logger.info("email {}" +savedUser.getEmail());

         emailService.sendEmail(
            savedUser.getEmail(), "Verify Account : Smart Contact Manager", emailLink);

         return savedUser;
    }

    @Override
    public Optional<User> getUserById(String id) {
       return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user1 = userRepo.findById(user.getId()).orElseThrow(()-> 
        new ResourceNotFoundException("User not found"));
        // update user1 from user
        user1.setName(user.getName());
        user1.setEmail(user.getEmail());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        user1.setAbout(user.getAbout());
        user1.setPhoneNumber(user.getPhoneNumber());
        user1.setProfilePic(user.getProfilePic());
        user1.setEnabled(user.isEnabled());
        user1.setEmailVerified(user.isEmailVerified());
        user1.setPhoneVerified(user.isPhoneVerified());
        user1.setProvider(user.getProvider());
        user1.setProviderUderId(user.getProviderUderId());

        System.out.println(user1.getPassword());
        // save the user to the database
        User saveUser = userRepo.save(user1);

        return Optional.ofNullable(saveUser);

    }

    @Override
    public void deleteUser(String id) {
        User user1 = userRepo.findById(id).orElseThrow(()-> 
        new ResourceNotFoundException("User not found"));

        userRepo.delete(user1);
    }

    @Override
    public boolean isUserExist(String id) {
        User user = userRepo.findById(id).orElse(null);

        return user != null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);

        return user != null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        
        return this.userRepo.findByEmail(email).orElse(null);
    }

    
}
