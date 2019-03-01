package com.toulios.projects.movierama.services;

import com.toulios.projects.movierama.exceptions.BadRequestException;
import com.toulios.projects.movierama.model.User;
import com.toulios.projects.movierama.payload.ApiResponse;
import com.toulios.projects.movierama.payload.JwtAuthenticationResponse;
import com.toulios.projects.movierama.payload.LoginRequest;
import com.toulios.projects.movierama.payload.SignUpRequest;
import com.toulios.projects.movierama.repositories.UserRepository;
import com.toulios.projects.movierama.security.JwtTokenProvider;
import com.toulios.projects.movierama.utils.TestHelper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.toulios.projects.movierama.utils.TestConstants.TEST_EMAIL;
import static com.toulios.projects.movierama.utils.TestConstants.TEST_JWT;
import static com.toulios.projects.movierama.utils.TestConstants.TEST_NAME;
import static com.toulios.projects.movierama.utils.TestConstants.TEST_PASSWORD;
import static com.toulios.projects.movierama.utils.TestConstants.TEST_USERNAME;
import static com.toulios.projects.movierama.utils.TestHelper.createLoginRequest;
import static com.toulios.projects.movierama.utils.TestHelper.createSignUpRequest;
import static com.toulios.projects.movierama.utils.TestHelper.createUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTests {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtTokenProvider tokenProvider;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = BadRequestException.class)
    public void test_signIn_failure_validation_null_request(){
        // given



        // when
        authenticationService.signin(null);


        // then
    }

    @Test(expected = BadRequestException.class)
    public void test_signIn_failure_validation_empty_username(){
        // given

        LoginRequest loginRequest =  new LoginRequest();
        loginRequest.setUsernameOrEmail(StringUtils.EMPTY);

        // when
        authenticationService.signin(loginRequest);


        // then
    }

    @Test(expected = BadRequestException.class)
    public void test_signIn_failure_validation_empty_password(){
        // given

        LoginRequest loginRequest =  new LoginRequest();
        loginRequest.setPassword(StringUtils.EMPTY);
        loginRequest.setUsernameOrEmail(TEST_USERNAME);

        // when
        authenticationService.signin(loginRequest);


        // then
    }

    @Test
    public void test_signIn_success(){
        // given

        LoginRequest loginRequest =  createLoginRequest();
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenProvider.generateToken(Mockito.any(Authentication.class))).thenReturn(TEST_JWT);

        // when
        ResponseEntity<JwtAuthenticationResponse> apiResponseResponseEntity = authenticationService.signin(loginRequest);


        // then
        assertEquals(TEST_JWT, apiResponseResponseEntity.getBody().getAccessToken());
    }

    @Test(expected = BadRequestException.class)
    public void test_signUp_failure_validation_null_request(){
        // given



        // when
        authenticationService.signup(null);


        // then
    }

    @Test(expected = BadRequestException.class)
    public void test_signUp_failure_validation_empty_email(){
        // given

        SignUpRequest signUpRequest = new SignUpRequest();

        // when
        authenticationService.signup(signUpRequest);


        // then
    }

    @Test(expected = BadRequestException.class)
    public void test_signUp_failure_validation_email_size(){
        // given

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(StringUtils.repeat("*", 45));

        // when
        authenticationService.signup(signUpRequest);


        // then
    }

    @Test(expected = BadRequestException.class)
    public void test_signUp_failure_validation_empty_username(){
        // given

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(TEST_EMAIL);
        // when
        authenticationService.signup(signUpRequest);


        // then
    }

    @Test(expected = BadRequestException.class)
    public void test_signUp_failure_validation_username_size(){
        // given

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(TEST_EMAIL);
        signUpRequest.setUsername(StringUtils.repeat("*",20));
        // when
        authenticationService.signup(signUpRequest);

        // then
    }

    @Test(expected = BadRequestException.class)
    public void test_signUp_failure_validation_empty_name(){
        // given

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(TEST_EMAIL);
        signUpRequest.setUsername(TEST_USERNAME);

        // when
        authenticationService.signup(signUpRequest);

        // then
    }

    @Test(expected = BadRequestException.class)
    public void test_signUp_failure_validation_name_size_over_maximum_limit(){
        // given

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(TEST_EMAIL);
        signUpRequest.setUsername(TEST_USERNAME);
        signUpRequest.setName(StringUtils.repeat("*", 50));

        // when
        authenticationService.signup(signUpRequest);

        // then
    }

    @Test(expected = BadRequestException.class)
    public void test_signUp_failure_validation_name_size_under_minimum_limit(){
        // given

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(TEST_EMAIL);
        signUpRequest.setUsername(TEST_USERNAME);
        signUpRequest.setName(StringUtils.repeat("*", 3));

        // when
        authenticationService.signup(signUpRequest);

        // then
    }

    @Test(expected = BadRequestException.class)
    public void test_signUp_failure_validation_empty_password(){
        // given

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(TEST_EMAIL);
        signUpRequest.setUsername(TEST_USERNAME);
        signUpRequest.setName(TEST_NAME);

        // when
        authenticationService.signup(signUpRequest);


        // then
    }

    @Test(expected = BadRequestException.class)
    public void test_signUp_failure_validation_password_size(){
        // given

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(TEST_EMAIL);
        signUpRequest.setUsername(TEST_USERNAME);
        signUpRequest.setName(TEST_NAME);
        signUpRequest.setPassword(StringUtils.repeat("*",20));
        // when
        authenticationService.signup(signUpRequest);

        // then
    }

    @Test
    public void test_signUp_failure_user_exists_given_username(){
        // given

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(TEST_EMAIL);
        signUpRequest.setUsername(TEST_USERNAME);
        signUpRequest.setName(TEST_NAME);
        signUpRequest.setPassword(TEST_PASSWORD);

        when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);

        // when
        ResponseEntity<ApiResponse> apiResponseResponseEntity = authenticationService.signup(signUpRequest);


        // then
        assertEquals(HttpStatus.BAD_REQUEST, apiResponseResponseEntity.getStatusCode());
    }

    @Test
    public void test_signUp_failure_user_exists_given_email(){
        // given

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(TEST_EMAIL);
        signUpRequest.setUsername(TEST_USERNAME);
        signUpRequest.setName(TEST_NAME);
        signUpRequest.setPassword(TEST_PASSWORD);

        when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        // when
        ResponseEntity<ApiResponse> apiResponseResponseEntity = authenticationService.signup(signUpRequest);


        // then
        assertEquals(HttpStatus.BAD_REQUEST, apiResponseResponseEntity.getStatusCode());
    }

    @Test
    public void test_signUp_success(){
        // given

        SignUpRequest signUpRequest = createSignUpRequest();
        User user = createUser();

        when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        ResponseEntity<ApiResponse> apiResponseResponseEntity = authenticationService.signup(signUpRequest);


        // then
        assertEquals(true, apiResponseResponseEntity.getBody().getSuccess());
    }


}
