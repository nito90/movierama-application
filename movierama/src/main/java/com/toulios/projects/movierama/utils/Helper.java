package com.toulios.projects.movierama.utils;

import com.toulios.projects.movierama.model.Movie;
import com.toulios.projects.movierama.model.User;
import com.toulios.projects.movierama.payload.MovieResponse;
import com.toulios.projects.movierama.payload.UserSummary;
import org.apache.commons.lang3.StringUtils;

public final class Helper {

    private Helper(){

    }

    /**
     * Transform a Movie object to Movieresponse object
     *
     * @param movie
     * @param creator
     * @param userVote
     * @return
     */
    public static MovieResponse movieToMovieresponseMapper(Movie movie, User creator, String userVote){
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setId(movie.getId());
        movieResponse.setDescription(movie.getDescription());
        movieResponse.setTitle(movie.getTitle());
        movieResponse.setCreationDateTime(movie.getCreatedAt());
        movieResponse.setTotalLikes(movie.getNumOfLikes());
        movieResponse.setTotalhates(movie.getNumOfHates());
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName(), 0, 0);
        movieResponse.setCreatedBy(creatorSummary);

        if( StringUtils.isNotEmpty(userVote) ) {
            movieResponse.setSelectedChoice(userVote);
        }

        return movieResponse;
    }

}
