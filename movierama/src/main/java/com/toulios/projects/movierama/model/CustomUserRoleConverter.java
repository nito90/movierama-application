package com.toulios.projects.movierama.model;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Converts a set of Strings to String value
 * and vice-versa for database serialization
 * deserialization
 */
@Converter
public class CustomUserRoleConverter implements AttributeConverter<Set<String>, String> {

    private static final String SEPARATOR = ", ";

    @Override
    public String convertToDatabaseColumn(Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        if(!roles.isEmpty()){
            int numOfRoles = roles.size();
            int i = 0;
            for(String role : roles){
                sb.append(role);
                if(i < (numOfRoles-1)){
                    sb.append(SEPARATOR);
                }
                i++;
            }
        }


        return sb.toString();
    }

    @Override
    public Set<String> convertToEntityAttribute(String rolesString) {
        if (rolesString == null || rolesString.isEmpty()) {
            return Collections.emptySet();
        }

        String[] roleValues = rolesString.split(SEPARATOR);

        if (roleValues == null || roleValues.length == 0) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(roleValues));
    }
}