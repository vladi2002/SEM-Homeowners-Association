package nl.tudelft.sem.activity.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing an authentication response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponseModel {
    private String token;
}