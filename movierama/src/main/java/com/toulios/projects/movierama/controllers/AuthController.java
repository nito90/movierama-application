package com.toulios.projects.movierama.controllers;

import com.toulios.projects.movierama.payload.ApiResponse;
import com.toulios.projects.movierama.payload.JwtAuthenticationResponse;
import com.toulios.projects.movierama.payload.LoginRequest;
import com.toulios.projects.movierama.payload.SignUpRequest;
import com.toulios.projects.movierama.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * It controls the authorization calls such as user's sign-in/sign-up.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Authenticates and sign in the user
     * 
     * @param loginRequest
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authenticationService.signin(loginRequest);
    }

    /**
     * Create a new user
     *
     * @param signUpRequest
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.signup(signUpRequest);
    }


}
