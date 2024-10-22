package com.scm.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessegeType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/user/contact")
public class ContactController {

    private Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;


    // add contact

    @GetMapping("/add")
    public String addContactView(Model model) {

        ContactForm contactForm = new ContactForm();
       

        model.addAttribute("contactForm", contactForm);

        return "user/add_contact";
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact( @Valid@ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication, HttpSession session) {

        // process the form data

        // validate the form
        if (result.hasErrors()) {

            Message message = Message.builder().
		content("Please solve the following error").type(MessegeType.red
		).build();

        session.setAttribute("message",message);

            return "user/add_contact";
        }

        // got the user from authentication
        String username = Helper.getEmailOfLoggedInUser(authentication);
        //form to contact
        User user = userService.getUserByEmail(username);

        // process the image
        logger.info("file information : {}", contactForm.getContactImage().getOriginalFilename());
        // file upload karne ka code

        String fileName = UUID.randomUUID().toString();
        String fileUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);


        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setPicture(fileUrl);
        contact.setCloudinaryImagePublicId(fileName);

        contactService.save(contact);


        // save the contact picture url

        //set the message for displayed view
        Message message = Message.builder().
		content("You have successfully add a new contact").type(MessegeType.green
		).build();

        session.setAttribute("message", message);

        
        return "redirect:/user/contact/add";

    }


    @RequestMapping
    public String viewContact(
        @RequestParam(value = "page", defaultValue = "0") int page,@RequestParam(value = "size", defaultValue = ""+AppConstants.PAGE_SIZE) int size,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy, @RequestParam(value = "direction", defaultValue = "asc") String direction,
        Model model, Authentication authentication) {

        // load all the user contacts
        //get the userID
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

       Page<Contact> pageContact = contactService.getByUser(user,page,size,sortBy,direction);

       model.addAttribute("pageContact", pageContact);
       model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "user/contacts";
    }

}
