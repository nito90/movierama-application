package com.toulios.projects.movierama.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;

/**
 * Bussiness object to provide to the client a movie's details
 */
public class MovieResponse {
    private Long id;
    private String description;
    private String title;
    private UserSummary createdBy;
    private Date creationDateTime;

    private int totalLikes;
    private int totalhates;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String selectedChoice;

    public MovieResponse() {
    }

    public MovieResponse(Long id, String description, String title, UserSummary createdBy, Date creationDateTime, int totalLikes, int totalhates, String selectedChoice) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.createdBy = createdBy;
        this.creationDateTime = creationDateTime;
        this.totalLikes = totalLikes;
        this.totalhates = totalhates;
        this.selectedChoice = selectedChoice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserSummary getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserSummary createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getTotalhates() {
        return totalhates;
    }

    public void setTotalhates(int totalhates) {
        this.totalhates = totalhates;
    }

    public String getSelectedChoice() {
        return selectedChoice;
    }

    public void setSelectedChoice(String selectedChoice) {
        this.selectedChoice = selectedChoice;
    }
}
