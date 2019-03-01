package com.toulios.projects.movierama.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Bussiness object to hold the data of a new movie's addition
 */
public class AddMovieRequest implements Serializable {

    private static final long serialVersionUID = 5231682803090863724L;

    private String title;

    private String description;

    public AddMovieRequest() {
    }

    public AddMovieRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
