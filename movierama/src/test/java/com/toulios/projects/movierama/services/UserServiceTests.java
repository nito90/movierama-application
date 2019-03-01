package com.toulios.projects.movierama.services;

import com.toulios.projects.movierama.exceptions.ResourceNotFoundException;
import com.toulios.projects.movierama.model.User;
import com.toulios.projects.movierama.payload.AddMovieRequest;
import com.toulios.projects.movierama.payload.UserSummary;
import com.toulios.projects.movierama.repositories.MovieRepository;
import com.toulios.projects.movierama.repositories.OpinionRepository;
import com.toulios.projects.movierama.repositories.UserRepository;
import com.toulios.projects.movierama.security.UserPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;

import static com.toulios.projects.movierama.utils.TestConstants.TEST_EMAIL;
import static com.toulios.projects.movierama.utils.TestConstants.TEST_NAME;
import static com.toulios.projects.movierama.utils.TestConstants.TEST_PASSWORD;
import static com.toulios.projects.movierama.utils.TestConstants.TEST_USERNAME;
import static com.toulios.projects.movierama.utils.TestHelper.createUser;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {


    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    MovieRepository movieRepository;

    @Mock
    private OpinionRepository opinionRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_addAMovie_failure_not_found_username(){
        // given

        AddMovieRequest addMovieRequest = new AddMovieRequest();
        addMovieRequest.setTitle("*");
        addMovieRequest.setDescription("*");

        // when
        userService.addMovie(TEST_USERNAME, addMovieRequest);

    }

    @Test
    public void test_addAMovie_success(){
        // given

        AddMovieRequest addMovieRequest = new AddMovieRequest();
        addMovieRequest.setTitle("*");
        addMovieRequest.setDescription("*");
        User user = createUser();

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.ofNullable(user));

        // when
        User resultedUser = userService.addMovie(TEST_USERNAME, addMovieRequest);

        // then
        assertEquals(1, resultedUser.getMovies().size());
    }

    @Test
    public void test_getCurrentUserSummary(){
        // given

        UserPrincipal userPrincipal = UserPrincipal.create(createUser());
        when(movieRepository.countByUserId(Mockito.anyLong())).thenReturn(1l);
        when(opinionRepository.countByUserId(Mockito.anyLong())).thenReturn(1l);

        // when

        UserSummary actualUsersummary = userService.getCurrentUserSummary(userPrincipal);

        // then
        assertEquals(1l, actualUsersummary.getTotalCreatedMovies());
        assertEquals(1l, actualUsersummary.getTotalMovieOpinions());

    }

    @Test
    public void test_getUsernameVailability_false(){
        // given

        when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(true);

        // when

        Boolean actualResult = userService.getUsernameVailability(TEST_USERNAME);

        // then
        assertFalse(actualResult);
    }

    @Test
    public void test_getUsernameVailability_true(){
        // given

        when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(false);

        // when

        Boolean actualResult = userService.getUsernameVailability(TEST_USERNAME);

        // then
        assertTrue(actualResult);
    }

    @Test
    public void test_getEmailAvailability_false(){
        // given

        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

        // when

        Boolean actualResult = userService.getEmailAvailability(TEST_EMAIL);

        // then
        assertFalse(actualResult);
    }

    @Test
    public void test_getEmailAvailability_true(){
        // given

        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);

        // when

        Boolean actualResult = userService.getEmailAvailability(TEST_EMAIL);

        // then
        assertTrue(actualResult);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_getUserSummaryByUsername_failure_user_does_not_exist(){
        // given

        // when
        userService.getUserSummaryByUsername(TEST_USERNAME);

    }

    @Test
    public void test_getUserSummaryByUsername_success(){
        // given

        User user = createUser();
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.ofNullable(user));

        // when

        UserSummary actualUserSummary = userService.getUserSummaryByUsername(TEST_USERNAME);

        // then

        assertEquals(user.getUsername(), actualUserSummary.getUsername());
    }

}
