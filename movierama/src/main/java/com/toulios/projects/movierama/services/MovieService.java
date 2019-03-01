package com.toulios.projects.movierama.services;

import com.toulios.projects.movierama.exceptions.ResourceNotFoundException;
import com.toulios.projects.movierama.exceptions.ResourceValidationException;
import com.toulios.projects.movierama.model.Movie;
import com.toulios.projects.movierama.model.Opinion;
import com.toulios.projects.movierama.model.OpinionChoice;
import com.toulios.projects.movierama.model.User;
import com.toulios.projects.movierama.payload.MovieResponse;
import com.toulios.projects.movierama.payload.OpinionRequest;
import com.toulios.projects.movierama.payload.PagedResponse;
import com.toulios.projects.movierama.repositories.MovieRepository;
import com.toulios.projects.movierama.repositories.OpinionRepository;
import com.toulios.projects.movierama.repositories.UserRepository;
import com.toulios.projects.movierama.security.UserPrincipal;
import com.toulios.projects.movierama.utils.AppConstants;
import com.toulios.projects.movierama.utils.Helper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.toulios.projects.movierama.utils.AppConstants.MOVIE_MODEL;
import static com.toulios.projects.movierama.utils.AppConstants.OPINION_MODEL;
import static com.toulios.projects.movierama.utils.AppConstants.USER_MODEL;

@Service
public class MovieService {

    private static Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private OpinionRepository opinionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private static final String CREATED_AT_FILTER = "createdAt";
    private static final String NUM_OF_LIKES_FILTER = "numOfLikes";
    private static final String NUM_OF_HATES_FILTER = "numOfHates";
    private static final String TITLE_FILTER = "title";

    /**
     * Return all the movies with pagination with information about if the
     * specific user has express his opinion about each movie
     *
     * @param currentUser
     * @param page
     * @param size
     * @return
     */
    public PagedResponse<MovieResponse> getAllMovies(UserPrincipal currentUser, int page, int size){
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT_FILTER,TITLE_FILTER);
        Page<Movie> movies = movieRepository.findAll(pageable);

        if(movies.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), movies.getNumber(),
                    movies.getSize(), movies.getTotalElements(), movies.getTotalPages(), movies.isLast());
        }

        List<Long> movieIds = movies.map(Movie::getId).getContent();
        Map<Long, String> movieUserOpinionMap = getMovieUserOpinionMap(currentUser, movieIds);
        Map<Long, User> creatorMap = getMovieCreatorMap(movies.getContent());

        List<MovieResponse> movieResponses = getMoviesResponses( movies, movieUserOpinionMap, creatorMap);
        return new PagedResponse<>(movieResponses, movies.getNumber(),
                movies.getSize(), movies.getTotalElements(), movies.getTotalPages(), movies.isLast());

    }

    /**
     * Return all the movies with pagination and ordered by the specific filter and by title
     * with information about if the specific user has express his opinion about each movie
     *
     * @param currentUser
     * @param page
     * @param size
     * @param filter
     * @return
     */
    public PagedResponse<MovieResponse> getAllMoviesOrdered(UserPrincipal currentUser, int page, int size, String filter){
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT_FILTER,TITLE_FILTER);
        if(StringUtils.isNotEmpty(filter)){
            if(OpinionChoice.LIKE.name().equalsIgnoreCase(filter)){
                pageable = PageRequest.of(page, size, Sort.Direction.DESC, NUM_OF_LIKES_FILTER,TITLE_FILTER);
            }
            if(OpinionChoice.HATE.name().equalsIgnoreCase(filter)){
                pageable = PageRequest.of(page, size, Sort.Direction.DESC, NUM_OF_HATES_FILTER, TITLE_FILTER);
            }
        }

        Page<Movie> movies = movieRepository.findAll(pageable);

        if(movies.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), movies.getNumber(),
                    movies.getSize(), movies.getTotalElements(), movies.getTotalPages(), movies.isLast());
        }

        List<Long> movieIds = movies.map(Movie::getId).getContent();
        Map<Long, String> movieUserOpinionMap = getMovieUserOpinionMap(currentUser, movieIds);
        Map<Long, User> creatorMap = getMovieCreatorMap(movies.getContent());

        List<MovieResponse> movieResponses = getMoviesResponses( movies, movieUserOpinionMap, creatorMap);
        return new PagedResponse<>(movieResponses, movies.getNumber(),
                movies.getSize(), movies.getTotalElements(), movies.getTotalPages(), movies.isLast());

    }

    /**
     * Return all the movies with pagination and ordered by the specific filter and by title
     * with information about if the specific user has express his opinion about each movie
     *
     * @param username
     * @param currentUser
     * @param page
     * @param size
     * @return
     */
    public PagedResponse<MovieResponse> getMoviesCreatedByUser(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(USER_MODEL, "username", username));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT_FILTER);
        Page<Movie> movies = movieRepository.findByUserId(user.getId(), pageable);

        if (movies.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), movies.getNumber(),
                    movies.getSize(), movies.getTotalElements(), movies.getTotalPages(), movies.isLast());
        }

        List<Long> movieids = movies.map(Movie::getId).getContent();
        Map<Long, String> movieUserOpinionMap = getMovieUserOpinionMap(currentUser, movieids);

        List<MovieResponse> movieResponses = getMoviesResponses( movies, movieUserOpinionMap, user);
        return new PagedResponse<>(movieResponses, movies.getNumber(),
                movies.getSize(), movies.getTotalElements(), movies.getTotalPages(), movies.isLast());
    }

    /**
     * Return all the movies with user's opinion
     * @param username
     * @param currentUser
     * @param page
     * @param size
     * @return
     */
    public PagedResponse<MovieResponse> getOpinionsByUser(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(USER_MODEL, "username", username));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT_FILTER);
        Page<Long> userSelectedMovieIds = opinionRepository.findSelectedMovieIdsByUserId(user.getId(), pageable);

        if (userSelectedMovieIds.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), userSelectedMovieIds.getNumber(),
                    userSelectedMovieIds.getSize(), userSelectedMovieIds.getTotalElements(),
                    userSelectedMovieIds.getTotalPages(), userSelectedMovieIds.isLast());
        }

        List<Long> movieIds = userSelectedMovieIds.getContent();

        Sort sort = new Sort(Sort.Direction.DESC, CREATED_AT_FILTER);
        List<Movie> movies = movieRepository.findByIdIn(movieIds, sort);

        Map<Long, String> movieUserOpinionMap = getMovieUserOpinionMap(currentUser, movieIds);
        Map<Long, User> creatorMap = getMovieCreatorMap(movies);


        List<MovieResponse> movieResponses = getMoviesResponses( movies,  movieUserOpinionMap, creatorMap);
        return new PagedResponse<>(movieResponses, userSelectedMovieIds.getNumber(), userSelectedMovieIds.getSize(), userSelectedMovieIds.getTotalElements(), userSelectedMovieIds.getTotalPages(), userSelectedMovieIds.isLast());
    }

    /**
     * The user express his/her opinion about the specified movie
     *
     * @param movieId
     * @param opinionRequest
     * @param currentUser
     * @return
     */
    public MovieResponse setOpinionUpdatedMovie(Long movieId, OpinionRequest opinionRequest, UserPrincipal currentUser) {

        Opinion opinion = opinionRepository.findByUserIdAndMovieId(currentUser.getId(), movieId);
        MovieResponse movieResponse = null;
        if(opinion == null){
            movieResponse = getMovieResponseFromNewOpinion(movieId, opinionRequest, currentUser);
        }
        else {
            movieResponse = getMovieResponseAndChangeOpinion(opinion, movieId, opinionRequest, currentUser);
        }
        return movieResponse;
    }

    private MovieResponse getMovieResponseAndChangeOpinion(Opinion existedOpinion, Long movieId, OpinionRequest opinionRequest, UserPrincipal currentUser) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException(MOVIE_MODEL, "id", movieId));

        userRepository.findById(currentUser.getId())
                .orElseThrow(() ->new ResourceNotFoundException(USER_MODEL, "id", currentUser.getId()) );

        if(existedOpinion.getOpinionChoice().name().equals(opinionRequest.getOpinionText())){
            throw new ResourceValidationException(OPINION_MODEL, "For movie:" +movie.getTitle() + "you have already voted:"+opinionRequest.getOpinionText());
        }

        if(OpinionChoice.LIKE.equals(existedOpinion.getOpinionChoice()) && OpinionChoice.HATE.name().equals(opinionRequest.getOpinionText())){
            movie.setNumOfLikes(movie.getNumOfLikes() - 1);
            movie.setNumOfHates(movie.getNumOfHates() + 1);
        }
        else if(OpinionChoice.HATE.equals(existedOpinion.getOpinionChoice()) && OpinionChoice.LIKE.name().equals(opinionRequest.getOpinionText())){
            movie.setNumOfLikes(movie.getNumOfLikes() + 1);
            movie.setNumOfHates(movie.getNumOfHates() - 1);
        }

        movie = movieRepository.save(movie);
        existedOpinion.setMovie(movie);
        existedOpinion.setOpinionChoice(OpinionChoice.valueOf(opinionRequest.getOpinionText()));
        existedOpinion = opinionRepository.save(existedOpinion);


        Long creatorId = movie.getUser().getId();

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_MODEL, "id", creatorId));


        return Helper.movieToMovieresponseMapper(movie, creator, existedOpinion.getOpinionChoice().name());
    }

    /**
     * Clears the user's opinion about the specified movie
     *
     * @param movieId
     * @param currentUser
     * @return
     */
    public MovieResponse clearOpinion(Long movieId, UserPrincipal currentUser){
        Opinion opinion = opinionRepository.findByUserIdAndMovieId(currentUser.getId(), movieId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException(MOVIE_MODEL, "id", movieId));

        userRepository.findById(currentUser.getId())
                .orElseThrow(() ->new ResourceNotFoundException(USER_MODEL, "id", currentUser.getId()) );

        if(opinion != null){
            if(OpinionChoice.LIKE.equals(opinion.getOpinionChoice())){
                movie.setNumOfLikes(movie.getNumOfLikes()-1);
            }
            else if(OpinionChoice.HATE.equals(opinion.getOpinionChoice())){
                movie.setNumOfHates(movie.getNumOfHates()-1);
            }
            opinionRepository.delete(opinion);
            movie = movieRepository.save(movie);
        }

        Long creatorId = movie.getUser().getId();

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_MODEL, "id", creatorId));

        return Helper.movieToMovieresponseMapper(movie, creator, null);
    }

    private MovieResponse getMovieResponseFromNewOpinion(Long movieId, OpinionRequest opinionRequest, UserPrincipal currentUser) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException(MOVIE_MODEL, "id", movieId));

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() ->new ResourceNotFoundException(USER_MODEL, "id", currentUser.getId()) );

        Opinion newOpinion = new Opinion();
        newOpinion.setMovie(movie);
        newOpinion.setUser(user);
        newOpinion.setOpinionChoice(OpinionChoice.valueOf(opinionRequest.getOpinionText()));
        newOpinion = opinionRepository.save(newOpinion);


        if(OpinionChoice.LIKE.equals(newOpinion.getOpinionChoice())){
            movie.setNumOfLikes(movie.getNumOfLikes()+1);
        }
        else if(OpinionChoice.HATE.equals(newOpinion.getOpinionChoice())){
            movie.setNumOfHates(movie.getNumOfHates()+1);
        }

        movie = movieRepository.save(movie);
        Long creatorId = movie.getUser().getId();

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_MODEL, "id", creatorId));


        return Helper.movieToMovieresponseMapper(movie, creator, newOpinion.getOpinionChoice().name());
    }

    /**
     * Validates the page value and size against app values
     *
     * @param page
     * @param size
     */
    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new ResourceValidationException(MOVIE_MODEL , "Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new ResourceValidationException(MOVIE_MODEL , "Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    /**
     * Get a map with users's opinion per movie with pair values (movieId, opinion)
     *
     * @param currentUser
     * @param movieIds
     * @return
     */
    private Map<Long, String> getMovieUserOpinionMap(UserPrincipal currentUser, List<Long> movieIds) {
        Map<Long, String> movieUserOpinionsMap = null;


        if(currentUser != null) {
            List<Opinion> userOpinions = opinionRepository.findByUserIdAndMoviedIn(currentUser.getId(), movieIds);
            movieUserOpinionsMap = userOpinions
                    .stream()
                    .collect(Collectors.toMap(opinion -> opinion.getMovie().getId(), opinion -> opinion.getOpinionChoice().name()));
        }
        return movieUserOpinionsMap;
    }

    /**
     * Get a map with movies creators (userid, user)
     * @param movies
     * @return
     */
    private Map<Long, User> getMovieCreatorMap(List<Movie> movies) {
        List<Long> userCreatorIds = movies.stream()
                .map(movie -> movie.getUser().getId())
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(userCreatorIds);

        return creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }

    /**
     * Transform a movie's information to movieresponse
     *
     * @param movies
     * @param movieUserOpinionMap
     * @param creatorMap
     * @return
     */
    private List<MovieResponse> getMoviesResponses(Page<Movie> movies, Map<Long, String> movieUserOpinionMap, Map<Long, User> creatorMap){
        List<MovieResponse> movieResponses = new ArrayList<>();
        for(Movie movie : movies.getContent()){
            MovieResponse  movieResponse = Helper.movieToMovieresponseMapper(
                    movie,
                    creatorMap.get(movie.getUser().getId()),
                    movieUserOpinionMap == null ? null : movieUserOpinionMap.getOrDefault(movie.getId(), null)
                    );
            movieResponses.add(movieResponse);
        }

        return movieResponses;

    }

    /**
     * Transform a movie's information to movieresponse
     *
     * @param movies
     * @param movieUserOpinionMap
     * @param creatorMap
     * @return
     */
    private List<MovieResponse> getMoviesResponses(List<Movie> movies, Map<Long, String> movieUserOpinionMap, Map<Long, User> creatorMap){
        List<MovieResponse> movieResponses = new ArrayList<>();
        for(Movie movie : movies){
            MovieResponse  movieResponse = Helper.movieToMovieresponseMapper(
                    movie,
                    creatorMap.get(movie.getUser().getId()),
                    movieUserOpinionMap == null ? null : movieUserOpinionMap.getOrDefault(movie.getId(), null)
            );
            movieResponses.add(movieResponse);
        }

        return movieResponses;

    }

    /**
     * Transform a movie's information to movieresponse
     *
     * @param movies
     * @param movieUserOpinionMap
     * @param creator
     * @return
     */
    private List<MovieResponse> getMoviesResponses(Page<Movie> movies, Map<Long, String> movieUserOpinionMap, User creator){
        List<MovieResponse> movieResponses = new ArrayList<>();
        for(Movie movie : movies.getContent()){
            MovieResponse  movieResponse = Helper.movieToMovieresponseMapper(
                    movie,
                    creator,
                    movieUserOpinionMap == null ? null : movieUserOpinionMap.getOrDefault(movie.getId(), null)
            );
            movieResponses.add(movieResponse);
        }

        return movieResponses;

    }

}
