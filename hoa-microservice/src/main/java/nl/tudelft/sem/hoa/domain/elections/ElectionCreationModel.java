package nl.tudelft.sem.hoa.domain.elections;

import lombok.Data;

/**
 * Model representing a registration request.
 */
@Data
public class ElectionCreationModel {
    private String electionId;
    private String hoaId;
}
