package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewUserRequest {
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;
    @NotBlank
    @Email
    @Length(min = 6, max = 254)
    private String email;

}
