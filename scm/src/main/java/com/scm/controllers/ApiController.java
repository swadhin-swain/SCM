package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.entities.Contact;
import com.scm.services.ContactService;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;

    Logger logger = LoggerFactory.getLogger(ApiController.class);


    // get contact

    @GetMapping("/contacts/{contactId}")
    public String getContact(@PathVariable String contactId, Model model) {
        
        Contact contact = this.contactService.getById(contactId);

        String name = contact.getName();

        logger.info("name {}", contact.getPhoneNumber());


        model.addAttribute("contact", contact);

        return "user/profiledetail";
    }
}
