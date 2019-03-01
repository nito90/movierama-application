package com.toulios.projects.movierama.controllers;

import com.toulios.projects.movierama.payload.MovieResponse;
import com.toulios.projects.movierama.payload.OpinionRequest;
import com.toulios.projects.movierama.payload.PagedResponse;
import com.toulios.projects.movierama.security.CurrentUser;
import com.toulios.projects.movierama.security.UserPrincipal;
import com.toulios.projects.movierama.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.toulios.projects.movierama.utils.AppConstants.DEFAULT_PAGE_NUMBER;
import static com.toulios.projects.movierama.utils.AppConstants.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping(value = "/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    /**
     * Return all the movies with pagination with information about if the
     * specific user has express his opinion about each movie
     *
     * @param currentUser
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public PagedResponse<MovieResponse> getMovies(@CurrentUser UserPrincipal currentUser,
                                                 @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                 @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return movieService.getAllMovies(currentUser, page, size);
    }

    /**
     * Return all the movies with pagination and ordered by the specific filter and by title
     * with information about if the specific user has express his opinion about each movie
     *
     * @param currentUser
     * @param filter
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/ordered/{filter}")
    public PagedResponse<MovieResponse> getMoviesOrderByFilter(@CurrentUser UserPrincipal currentUser,
                                                              @PathVariable String filter,
                                                              @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                              @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return movieService.getAllMoviesOrdered(currentUser, page, size, filter);
    }

    /**
     * The user express his/her opinion about the specified movie
     *
     * @param currentUser
     * @param movieId
     * @param opinionRequest
     * @return
     */
    @PostMapping("/{movieId}/opinion")
    @PreAuthorize("hasRole('USER')")
    public MovieResponse expressOpinion(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable Long movieId,
                                 @Valid @RequestBody OpinionRequest opinionRequest) {
        return movieService.setOpinionUpdatedMovie(movieId, opinionRequest, currentUser);
    }

    /**
     * Clears the user's opinion about the specified movie
     *
     * @param currentUser
     * @param movieId
     * @return
     */
    @PostMapping("/{movieId}/opinion/clear")
    @PreAuthorize("hasRole('USER')")
    public MovieResponse clearOpinion(@CurrentUser UserPrincipal currentUser,
                                  @PathVariable Long movieId) {
        return movieService.clearOpinion(movieId, currentUser);
    }
}

