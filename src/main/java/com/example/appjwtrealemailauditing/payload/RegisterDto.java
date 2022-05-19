package com.example.appjwtrealemailauditing.payload;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterDto {

    @NotNull
    @Size(min = 3,max = 50)//minimal va maximal qiymatini validatsiadan otqizadi va databazaga bervoradi
    private String firstName;//userning ismi

    @NotNull
    @Length(min = 3,max = 50)//bu ham size kabi bir  xil ish bajaradi
    private String lastName;//userning familyasi

    @NotNull
    @Email
    private String email;//example@gmail.com userning emaili(USERNAME SIFATIDA OLINADI)

    @NotNull
    private String password;
}
