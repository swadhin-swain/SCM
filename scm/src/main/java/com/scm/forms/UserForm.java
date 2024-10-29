package com.scm.forms;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForm {

    @NotBlank(message = "Username required")
    @Size(min = 3,message = "Min 3 characters required")
    private String name;

     @Email(message = "Invalid Email Address [example@gmail.com]")
    private String email;

    @NotBlank(message = "password required")
    @Size(min = 6, message = "Min 6 Characters required")
    private String password;

    @NotBlank(message = "About is required")
    private String about;

    @Size(min = 10,max = 12,message = "Invalid phone number")
    private String phoneNumber;

}
