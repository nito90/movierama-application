package com.toulios.projects.movierama.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Bussiness object to hold the data of a Opinion request which
 * holds the user's opinion
 */
public class OpinionRequest {

    private String opinionText;

    public String getOpinionText() {
        return opinionText;
    }

    public void setOpinionText(String opinionText) {
        this.opinionText = opinionText;
    }
}
