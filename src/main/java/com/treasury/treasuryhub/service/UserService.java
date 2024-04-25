package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.SignUpUserDto;
import com.treasury.treasuryhub.exception.UserAlreadyExistsException;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.UserRepository;
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

    public ResponseEntity<?> registerUser(SignUpUserDto signUpUserDto)
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

        userRepository.save(newUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public boolean verifyEmailAlreadyUsed(String email) {
        Optional<User> maybeUser = userRepository.findByEmail(email);
        return maybeUser.isPresent();
    }

    public UserDetails fetchCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (UserDetails) principal;
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
}
