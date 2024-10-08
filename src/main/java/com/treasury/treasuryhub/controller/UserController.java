package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.config.JwtConfig;
import com.treasury.treasuryhub.dto.CategoryTotalsDto;
import com.treasury.treasuryhub.dto.SignInResponseDto;
import com.treasury.treasuryhub.dto.SignInUserDto;
import com.treasury.treasuryhub.dto.SignUpUserDto;
import com.treasury.treasuryhub.exception.NoSuchUserException;
import com.treasury.treasuryhub.exception.UserAlreadyExistsException;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.UserRepository;
import com.treasury.treasuryhub.service.EmailService;
import com.treasury.treasuryhub.service.TransactionService;
import com.treasury.treasuryhub.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin(origins = {"http://localhost:3000", "https://treasury-hub-a7870221c88d.herokuapp.com/"})
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/register")
    public ResponseEntity<?> signUpUser(@RequestBody SignUpUserDto signUpUserDto)
            throws UserAlreadyExistsException, MessagingException {
        User newUser = userService.registerUser(signUpUserDto);
        Map<String, Object> model = new HashMap<>();
        model.put("name", newUser.getFirstName());
//        emailService.sendWelcomeEmail(newUser.getEmail(), model);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> signInUser(@RequestBody SignInUserDto signInUserDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInUserDto.getEmail(), signInUserDto.getPassword()));
        UserDetails userDetails = userService.loadUserByUsername(signInUserDto.getEmail());
        SignInResponseDto signInResponseDto = new SignInResponseDto();
        signInResponseDto.setToken(jwtConfig.generateToken(userDetails));
        return ResponseEntity.ok(signInResponseDto);
    }

    @GetMapping("/userdetails")
    public ResponseEntity<?> fetchUserDetails() {
        return ResponseEntity.ok(userService.fetchCurrentUserDetails());
    }

    @GetMapping("/user")
    public ResponseEntity<?> fetchUser() {
        return ResponseEntity.ok(userService.fetchCurrentUser());
    }

    @GetMapping("/user/all")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@RequestBody SignUpUserDto signUpUserDto, @PathVariable int id) throws NoSuchUserException {
        return new ResponseEntity<>(userService.updateUser(signUpUserDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) throws NoSuchUserException {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
