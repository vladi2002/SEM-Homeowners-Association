package nl.tudelft.sem.hoa.models;

import lombok.Data;

/**
 * Model representing an authentication request.
 */
@Data
public class AuthenticationRequestModel {
    private String netId;
    private String password;
}