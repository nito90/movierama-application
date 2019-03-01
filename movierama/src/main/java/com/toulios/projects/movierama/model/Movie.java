package com.toulios.projects.movierama.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Keeps the information of a movie entry from DB
 */
@Entity
@Table(name = "movies")
@EntityListeners(AuditingEntityListener.class)
public class Movie extends DateAudit implements Serializable {


    private static final long serialVersionUID = 8436394859533469611L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    @Size(max = 100)
    private String title;

    @Column
    @NotBlank
    @Size(max = 300)
    private String description;

    @Column
    private int numOfLikes;

    @Column
    private int numOfHates;

    @NotNull
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false, updatable = false)
    private User user;

    public Movie(){

    }

    public Movie( String title, String description, int numOfLikes, int numOfHates, User user) {
        this.title = title;
        this.description = description;
        this.numOfLikes = numOfLikes;
        this.numOfHates = numOfHates;
        this.user = user;
    }

    public Long getId() {
        return id;
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

    public int getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public int getNumOfHates() {
        return numOfHates;
    }

    public void setNumOfHates(int numOfHates) {
        this.numOfHates = numOfHates;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", numOfLikes=" + numOfLikes +
                ", numOfHates=" + numOfHates +
                ", user=" + user.getName() +
                '}';
    }
}
