package com.scm.helper;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {

        

        // agar email id password se login kiya hai toh : email kaise nikalenge

        if(authentication instanceof OAuth2AuthenticationToken) {

            var oauth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;

            var clientId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User)authentication.getPrincipal();

            String username = "";

            if (clientId.equalsIgnoreCase("google")) {
                //user can do sign in with google
                System.out.println("getting email from google");
                username = oauth2User.getAttribute("email").toString();
    

            } else if (clientId.equalsIgnoreCase("github")) {
              //user can do sign in with github
              System.out.println("getting email from github");
              username = oauth2User.getAttribute("email") != null ?
              oauth2User.getAttribute("email").toString() : oauth2User.getAttribute("login").toString() + "@gmail.com";
             

            }

            return username;

        } else {

            System.out.println("Getting email from local database");
            return authentication.getName();
        }
       

       
    }
}
