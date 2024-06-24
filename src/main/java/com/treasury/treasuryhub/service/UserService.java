package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.SignUpUserDto;
import com.treasury.treasuryhub.exception.NoSuchUserException;
import com.treasury.treasuryhub.exception.UserAlreadyExistsException;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(SignUpUserDto signUpUserDto)
        throws UserAlreadyExistsException {
        if(verifyEmailAlreadyUsed(signUpUserDto.getEmail())) {
            throw new UserAlreadyExistsException("There is already an account with this email in use.");
        }

        User newUser = new User();
        newUser.setFirstName(signUpUserDto.getFirstName());
        newUser.setLastName(signUpUserDto.getLastName());
        newUser.setEmail(signUpUserDto.getEmail());
//        newUser.setPassword(signUpUserDto.getPassword());
        newUser.setPassword(passwordEncoder.encode(signUpUserDto.getPassword()));
        newUser.setRole(signUpUserDto.getRole());

        return userRepository.save(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean verifyEmailAlreadyUsed(String email) {
        Optional<User> maybeUser = userRepository.findByEmail(email);
        return maybeUser.isPresent();
    }

    public UserDetails fetchCurrentUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (UserDetails) principal;
    }

    public User fetchCurrentUser() {
        UserDetails userDetails = fetchCurrentUserDetails();
        User user = userRepository.findByEmail(userDetails.getUsername()).get();
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found" ));
//        Set<String> roles = Set.of(user.getRole());
        String role = user.getRole();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(role)));
    }

    private Collection<? extends GrantedAuthority> authoritiesConverter(Set<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public User updateUser(SignUpUserDto signUpUserDto, int id) throws NoSuchUserException {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(signUpUserDto.getFirstName());
                    user.setLastName(signUpUserDto.getLastName());
                    user.setEmail(signUpUserDto.getEmail());
                    user.setPassword(passwordEncoder.encode(signUpUserDto.getPassword()));
                    user.setRole(signUpUserDto.getRole());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new NoSuchUserException("User with id " + id + " does not exist"));
    }

    public void deleteUser(int id) throws NoSuchUserException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserException("User with id " + id + " does not exist"));
        userRepository.delete(user);
    }
}
