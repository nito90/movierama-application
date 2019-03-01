package com.toulios.projects.movierama.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

/**
 * Keeps the information of a user's opinion entry from DB
 */
@Entity
@Table(name = "opinions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "movie_id",
                "user_id",
                "opinion_choice"
        })
})
public class Opinion extends DateAudit implements Serializable {

    private static final long serialVersionUID = -9206194862600597222L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="opinion_choice")
    @Enumerated(EnumType.STRING)
    private OpinionChoice opinionChoice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    public Opinion() {
    }

    public Opinion(OpinionChoice opinionChoice, User user, Movie movie) {
        super();
        this.opinionChoice = opinionChoice;
        this.user = user;
        this.movie = movie;
    }

    public Long getId() {
        return id;
    }

    public OpinionChoice getOpinionChoice() {
        return opinionChoice;
    }

    public void setOpinionChoice(OpinionChoice opinionChoice) {
        this.opinionChoice = opinionChoice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
