package com.sgyjdev.studyolle.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SignInForm {

    @NotBlank
    private String username;

    @NotBlank
    @Length(min = 8, max = 50)
    private String password;

}
