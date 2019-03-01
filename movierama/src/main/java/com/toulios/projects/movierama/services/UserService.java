package com.toulios.projects.movierama.services;

import com.toulios.projects.movierama.exceptions.ResourceNotFoundException;
import com.toulios.projects.movierama.model.Movie;
import com.toulios.projects.movierama.model.User;
import com.toulios.projects.movierama.payload.AddMovieRequest;
import com.toulios.projects.movierama.payload.UserSummary;
import com.toulios.projects.movierama.repositories.MovieRepository;
import com.toulios.projects.movierama.repositories.OpinionRepository;
import com.toulios.projects.movierama.repositories.UserRepository;
import com.toulios.projects.movierama.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    private OpinionRepository opinionRepository;

    /**
     * Add a new movie with creator the specified user
     *
     * @param username
     * @param movieRequest
     * @return
     */
    public User addMovie(String username, AddMovieRequest movieRequest){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), "username", username));

        Movie movie = new Movie(
                movieRequest.getTitle(),
                movieRequest.getDescription(),
                0,
                0, null);


        user.addMovie(movie);
        movie.setUser(user);

        userRepository.save(user);
        return user;
    }

    /**
     * Returns the current authorized user
     *
     * @param currentUser
     * @return
     */
    public UserSummary getCurrentUserSummary(UserPrincipal currentUser) {
        long totalMoviesCreated = movieRepository.countByUserId(currentUser.getId());
        long totalMoviesOpinions = opinionRepository.countByUserId(currentUser.getId());

        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName(), totalMoviesCreated, totalMoviesOpinions);
    }

    /**
     * Checks if a user exists with the specified username
     *
     * @param username
     * @return
     */
    public Boolean getUsernameVailability(String username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * Checks if a user exists with the specified email
     *
     * @param email
     * @return
     */
    public Boolean getEmailAvailability(String email) {
        return !userRepository.existsByEmail(email);
    }

    /**
     * Returns the information for the user with the specified username
     *
     * @param username
     * @return
     */
    public UserSummary getUserSummaryByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long totalMoviesCreated = movieRepository.countByUserId(user.getId());
        long totalMoviesOpinions = opinionRepository.countByUserId(user.getId());

        return new UserSummary(user.getId(), user.getUsername(), user.getName(), totalMoviesCreated, totalMoviesOpinions);
    }
}
