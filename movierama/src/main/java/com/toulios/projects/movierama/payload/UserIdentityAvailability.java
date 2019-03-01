package com.toulios.projects.movierama.payload;

/**
 * Bussiness object to provide to the client if the username or email is available to used
 */
public class UserIdentityAvailability {
    private Boolean available;

    public UserIdentityAvailability(Boolean available) {
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
