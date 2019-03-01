package com.toulios.projects.movierama.controllers;

import com.toulios.projects.movierama.model.User;
import com.toulios.projects.movierama.payload.AddMovieRequest;
import com.toulios.projects.movierama.payload.MovieResponse;
import com.toulios.projects.movierama.payload.PagedResponse;
import com.toulios.projects.movierama.payload.UserIdentityAvailability;
import com.toulios.projects.movierama.payload.UserSummary;
import com.toulios.projects.movierama.repositories.MovieRepository;
import com.toulios.projects.movierama.repositories.OpinionRepository;
import com.toulios.projects.movierama.repositories.UserRepository;
import com.toulios.projects.movierama.security.CurrentUser;
import com.toulios.projects.movierama.security.UserPrincipal;
import com.toulios.projects.movierama.services.MovieService;
import com.toulios.projects.movierama.services.UserService;
import com.toulios.projects.movierama.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private OpinionRepository opinionRepository;


    /**
     * Add a new movie with creator the specified user
     *
     * @param username
     * @param movie
     * @return
     */
    @PostMapping("/{username}/addMovie")
    @PreAuthorize("hasRole('ROLE_USER')")
    public User addMovie(@PathVariable(value = "username") String username, @RequestBody AddMovieRequest movie) {
        return userService.addMovie(username, movie);
    }

    /**
     * Returns the current authorized user
     *
     * @param currentUser
     * @return
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return userService.getCurrentUserSummary(currentUser);
    }

    /**
     * Checks if a user exists with the specified username
     *
     * @param username
     * @return
     */
    @GetMapping("/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = userService.getUsernameVailability(username);
        return new UserIdentityAvailability(isAvailable);
    }

    /**
     * Checks if a user exists with the specified email
     *
     * @param email
     * @return
     */
    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = userService.getEmailAvailability(email);
        return new UserIdentityAvailability(isAvailable);
    }

    /**
     * Returns the information for the user with the specified username
     *
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserSummary getUserProfile(@PathVariable(value = "username") String username) {
        return userService.getUserSummaryByUsername(username);
    }

    /**
     * Return all the movies created by the user
     *
     * @param username
     * @param currentUser
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/{username}/movies")
    @PreAuthorize("hasRole('ROLE_USER')")
    public PagedResponse<MovieResponse> getMoviesCreatedByUser(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return movieService.getMoviesCreatedByUser(username, currentUser, page, size);
    }

    /**
     * Return all the movies with user's opinion
     * @param username
     * @param currentUser
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/{username}/movies/opinions")
    @PreAuthorize("hasRole('ROLE_USER')")
    public PagedResponse<MovieResponse> getOpinions(@PathVariable(value = "username") String username,
                                                          @CurrentUser UserPrincipal currentUser,
                                                          @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                          @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return movieService.getOpinionsByUser(username, currentUser, page, size);
    }
}
