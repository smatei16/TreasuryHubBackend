package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.dto.RegisterUserDto;
import com.treasury.treasuryhub.exception.UserAlreadyExistsException;
import com.treasury.treasuryhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDto registerUserDto)
        throws UserAlreadyExistsException {
        return userService.registerUser(registerUserDto);
    }
}
