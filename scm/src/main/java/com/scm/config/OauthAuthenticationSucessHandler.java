package com.scm.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.List;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.repository.UserRepo;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.var;

@Component
public class OauthAuthenticationSucessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OauthAuthenticationSucessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OauthAuthenticationSucessHandler");

        // identify the provider

        var oauth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;

        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistrationId);

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        //printing the properties of login provider
        oauthUser.getAttributes().forEach((key, val)->{
            logger.info(key+"..."+val);
        });

       // set the default value
       User user = new User();
       user.setId(UUID.randomUUID().toString());
       user.setRoleList(List.of(AppConstants.ROLE_USER));
       user.setEmailVerified(true);
       user.setEnabled(true);
        
        if(authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            // google 
        //google attributes
        user.setEmail(oauthUser.getAttribute("email").toString());
        user.setProfilePic(oauthUser.getAttribute("picture"));
        user.setName(oauthUser.getAttribute("name").toString());
        user.setProviderUderId(oauthUser.getName());
        user.setProvider(Providers.GOOGLE);
        user.setPassword("dummy");
        user.setAbout("This account is created using Google");

        } 
        else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
            
            // github 
        //github attributes
        // if email is null then
        String email = oauthUser.getAttribute("email") != null ?
        oauthUser.getAttribute("email").toString() : oauthUser.getAttribute("login").toString() + "@gmail.com";

        String picture = oauthUser.getAttribute("avatar_url").toString();
        String name = oauthUser.getAttribute("login").toString();
        String providerUserId = oauthUser.getName();

        user.setEmail(email);
        user.setName(name);
        user.setProfilePic(picture);
        user.setProviderUderId(providerUserId);
        user.setProvider(Providers.GITHUB);
        user.setPassword("dummy");
        user.setAbout("This account is created using Github");
        }
        else if (authorizedClientRegistrationId.equalsIgnoreCase("linkedin")) {
            
        }
        else {
            logger.info("OauthAuthenticationSucessHandler : Unknown provider");
        }
        

        /* 

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        logger.info(user.getName());

        user.getAttributes().forEach((key, val) -> {
            logger.info("{} : {}", key, val);
        });

        logger.info(user.getAuthorities().toString());

        // Get user details from OAuth
        String email = user.getAttribute("email").toString();
        System.out.println("email"+email);
        String name = user.getAttribute("name").toString();
        System.out.println(name);
        String picture = user.getAttribute("picture").toString();

        // Create new User entity
        User user1 = new User();
        user1.setEmail(email);
        user1.setName(name);
        user1.setProfilePic(picture);
        user1.setPassword("password");
        user1.setId(UUID.randomUUID().toString());
        user1.setProvider(Providers.GOOGLE);
        user1.setEnabled(true);
        user1.setEmailVerified(true);
        user1.setProviderUderId(user.getName());
        user1.setRoleList(List.of(AppConstants.ROLE_USER));
        user1.setAbout("This account is created using Google.");

        // Check if the user already exists in the database
        User user2 = userRepo.findByEmail(email).orElse(null);

        if (user2 == null) {
            userRepo.save(user1); // Save the new user (user1)
            logger.info("User saved: " + email);
        }

        */

        // save the user
        User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (user2 == null) {
            userRepo.save(user); // Save the new user (user1)
            logger.info("User saved: " + user.getEmail());
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}
