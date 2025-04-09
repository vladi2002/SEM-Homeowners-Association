package nl.tudelft.sem.activity.models;

import lombok.Data;
import nl.tudelft.sem.activity.domain.Description;
import nl.tudelft.sem.activity.domain.Organizer;

/**
 * Model representing a registration request.
 */
@Data
public class ActivityRegistrationRequestModel {
    private String description;
    private String date;
}