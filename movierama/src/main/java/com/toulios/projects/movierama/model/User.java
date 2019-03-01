package com.toulios.projects.movierama.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Keeps the information of a user's user entry from DB
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User implements Serializable {

    private static final long serialVersionUID = 3561403665330559463L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Movie> movies;

    @Column
    @NotBlank
    @Size(max = 15)
    private String username;

    @Column
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @Convert(converter = CustomUserRoleConverter.class)
    private Set<String> roles;


    public User() {
    }

    public User(Long id, String name, Set<Movie> movies, String username, String email, String password, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.movies = movies;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void addMovie(Movie movie){
        if(movies.isEmpty()){
            movies = new HashSet<>();
        }

        if(movie != null){
            movies.add(movie);
        }
    }
}
