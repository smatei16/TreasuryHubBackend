package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.RegisterUserDto;
import com.treasury.treasuryhub.exception.UserAlreadyExistsException;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> registerUser(RegisterUserDto registerUserDto)
        throws UserAlreadyExistsException {
        if(verifyEmailAlreadyUsed(registerUserDto.getEmail())) {
            throw new UserAlreadyExistsException("There is already an account with this email in use.");
        }

        User newUser = new User();
        newUser.setFirstName(registerUserDto.getFirstName());
        newUser.setLastName(registerUserDto.getLastName());
        newUser.setEmail(registerUserDto.getEmail());
        newUser.setPassword(registerUserDto.getPassword());

        userRepository.save(newUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public boolean verifyEmailAlreadyUsed(String email) {
        Optional<User> maybeUser = userRepository.findByEmail(email);
        return maybeUser.isPresent();
    }
}
