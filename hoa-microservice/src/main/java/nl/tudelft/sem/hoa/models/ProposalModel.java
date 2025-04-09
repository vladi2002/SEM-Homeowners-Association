package nl.tudelft.sem.hoa.models;

import lombok.Data;

@Data
public class ProposalModel {
    String proposalId;
    String hoaId;
    int ruleId;
    String proposal;
    String proposalType;
}
