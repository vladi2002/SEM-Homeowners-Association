package nl.tudelft.sem.hoa.models;

import lombok.Data;

@Data
public class ProposalVoteModel {
    String proposalId;
    String hoaId;
    String decision;
}
