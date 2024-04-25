package com.treasury.treasuryhub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpUserDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
}
