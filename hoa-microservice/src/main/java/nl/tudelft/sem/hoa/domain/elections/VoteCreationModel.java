package nl.tudelft.sem.hoa.domain.elections;

import lombok.Data;

@Data
public class VoteCreationModel {
    private String electionId;
    private String hoaId;
    private String applicantId;
}
