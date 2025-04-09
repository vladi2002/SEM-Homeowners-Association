package nl.tudelft.sem.activity.models;

import lombok.Data;

/**
 * Model representing an authentication request.
 */
@Data
public class ResponseToActivityRequestModel {
    private String activityId;
    private String response;
}