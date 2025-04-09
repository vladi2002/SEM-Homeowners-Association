package nl.tudelft.sem.hoa.models;

import lombok.Data;


/**
 * Model representing a update request.
 */
@Data
public class UpdateRequestModel {
    private String netId;
    private String oldPassword;
    private String newPassword;
}

