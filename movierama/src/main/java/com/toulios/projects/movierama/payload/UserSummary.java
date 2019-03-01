package com.toulios.projects.movierama.payload;

/**
 * Bussiness object to provide to the client a user's details
 */
public class UserSummary {
    private Long id;
    private String username;
    private String name;
    private long totalCreatedMovies;
    private long totalMovieOpinions;

    public UserSummary() {
    }

    public UserSummary(Long id, String username, String name, long totalCreatedMovies, long totalMovieOpinions) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.totalCreatedMovies = totalCreatedMovies;
        this.totalMovieOpinions = totalMovieOpinions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalCreatedMovies() {
        return totalCreatedMovies;
    }

    public void setTotalCreatedMovies(long totalCreatedMovies) {
        this.totalCreatedMovies = totalCreatedMovies;
    }

    public long getTotalMovieOpinions() {
        return totalMovieOpinions;
    }

    public void setTotalMovieOpinions(long totalMovieOpinions) {
        this.totalMovieOpinions = totalMovieOpinions;
    }
}
