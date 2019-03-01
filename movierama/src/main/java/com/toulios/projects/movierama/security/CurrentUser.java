package com.toulios.projects.movierama.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Spring security provides an annotation called @AuthenticationPrincipal
 * to access the currently authenticated user in the controllers.
 *
 * The following CurrentUser annotation is a wrapper around @AuthenticationPrincipal
 * annotation
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}
