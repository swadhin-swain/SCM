package com.scm.forms;


import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactForm {

    @NotBlank(message = "Username required")
    @Size(min = 3,message = "Min 3 characters required")
    private String name;

    @NotBlank(message = "Email is required")
     @Email(message = "Invalid Email Address [example@gmail.com]")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;
    private boolean favorite;
    private String websiteLink;
    private String linkedInLink;
    private String description;

    // annotation create karenge jo file validate kare
    // size 
    // resolution
    
    private MultipartFile contactImage;
}
