package nl.tudelft.sem.activity.models;

import lombok.Data;

/**
 * Model representing an authentication request.
 */
@Data
public class ActivityRequestModel {
    private String username;
    private String password;
}