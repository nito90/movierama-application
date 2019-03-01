package com.toulios.projects.movierama.utils;

import com.toulios.projects.movierama.model.User;
import com.toulios.projects.movierama.payload.LoginRequest;
import com.toulios.projects.movierama.payload.SignUpRequest;

import java.util.HashSet;

import static com.toulios.projects.movierama.utils.TestConstants.TEST_EMAIL;
import static com.toulios.projects.movierama.utils.TestConstants.TEST_NAME;
import static com.toulios.projects.movierama.utils.TestConstants.TEST_PASSWORD;
import static com.toulios.projects.movierama.utils.TestConstants.TEST_USERNAME;

public final class TestHelper {

    public static final User createUser(){
        User user = new User(1l, TEST_NAME, new HashSet<>(), TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD, new HashSet<>());
        return user;
    }

    public static final SignUpRequest createSignUpRequest(){
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(TEST_EMAIL);
        signUpRequest.setUsername(TEST_USERNAME);
        signUpRequest.setName(TEST_NAME);
        signUpRequest.setPassword(TEST_PASSWORD);
        return signUpRequest;
    }

    public static final LoginRequest createLoginRequest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail(TEST_USERNAME);
        loginRequest.setPassword(TEST_PASSWORD);
        return loginRequest;
    }

    private TestHelper(){}
}
