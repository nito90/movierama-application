package com.toulios.projects.movierama.services;

import com.toulios.projects.movierama.exceptions.BadRequestException;
import com.toulios.projects.movierama.model.RoleName;
import com.toulios.projects.movierama.model.User;
import com.toulios.projects.movierama.payload.ApiResponse;
import com.toulios.projects.movierama.payload.JwtAuthenticationResponse;
import com.toulios.projects.movierama.payload.LoginRequest;
import com.toulios.projects.movierama.payload.SignUpRequest;
import com.toulios.projects.movierama.repositories.UserRepository;
import com.toulios.projects.movierama.security.JwtTokenProvider;

import java.net.URI;
import java.util.Collections;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<JwtAuthenticationResponse> signin(LoginRequest loginRequest) {

        validateLoginRequest(loginRequest);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    public ResponseEntity<ApiResponse> signup(SignUpRequest signUpRequest) {

        validateSignUpRequest(signUpRequest);

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(
                    new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(
                    false,
                    "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        String passwordValue = passwordEncoder.encode(signUpRequest.getPassword());

        // Creating user's account
        User user = new User(
                null,
                signUpRequest.getName(),
                null,
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordValue,
                Collections.singleton(RoleName.ROLE_USER.name()));

        User result = userRepository.save(user);

        return ResponseEntity.ok().body(
                new ApiResponse(
                        true,
                        "User registered successfully"));
    }

    private void validateLoginRequest(LoginRequest loginRequest){
        if(loginRequest == null){
            throw new BadRequestException("Login request should not be null.");
        }

        if(StringUtils.isBlank(loginRequest.getUsernameOrEmail())){
            throw new BadRequestException("Login request username should not be empty.");
        }

        if(StringUtils.isBlank(loginRequest.getPassword())){
            throw new BadRequestException("Login request password should not be empty.");
        }
    }

    private void validateSignUpRequest(SignUpRequest signUpRequest){
        if(signUpRequest == null){
            throw new BadRequestException("Sign up request should not be null.");
        }

        if(StringUtils.isBlank(signUpRequest.getEmail())){
            throw new BadRequestException("Sign up request email should not be empty.");
        }

        if(signUpRequest.getEmail().length() > 40){
            throw new BadRequestException("Sign up request email should be less than 40.");
        }

        if(StringUtils.isBlank(signUpRequest.getUsername())){
            throw new BadRequestException("Sign up request username should not be empty.");
        }

        if(signUpRequest.getUsername().length() > 15){
            throw new BadRequestException("Sign up request username should be less than 15.");
        }

        if(StringUtils.isBlank(signUpRequest.getName())){
            throw new BadRequestException("Sign up request name should not be empty.");
        }

        if(signUpRequest.getName().length() > 40){
            throw new BadRequestException("Sign up request name should be less than 40.");
        }

        if(signUpRequest.getName().length() < 4){
            throw new BadRequestException("Sign up request name should be more than 4.");
        }

        if(StringUtils.isBlank(signUpRequest.getPassword())){
            throw new BadRequestException("Sign up request password should not be empty.");
        }

        if(signUpRequest.getPassword().length() > 15){
            throw new BadRequestException("Sign up password name should be less than 15.");
        }

    }

}
