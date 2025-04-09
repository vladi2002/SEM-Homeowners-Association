package nl.tudelft.sem.hoa.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.hoa.domain.CreationDate;
import nl.tudelft.sem.hoa.domain.proposals.Proposal;
import nl.tudelft.sem.hoa.domain.proposals.ProposalType;
import nl.tudelft.sem.hoa.domain.proposals.RuleProposal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProposalResponseModel {

    String proposal;
    String type;
    int accept;
    int reject;
    int abstain;
    String creationDate;
    int ruleId;

    /**
     * Create response model for general proposals.
     *
     * @param proposal The proposal
     * @param type The type
     * @param accept The amount of accept votes
     * @param reject The amount of reject votes
     * @param abstain The amount of abstain votes
     * @param date The date the proposal was created on
     */
    public ProposalResponseModel(String proposal, ProposalType type, int accept,
                                 int reject, int abstain, CreationDate date) {
        this.proposal = proposal;
        this.type = type.name();
        this.accept = accept;
        this.reject = reject;
        this.abstain = abstain;
        this.creationDate = date.toString();
    }

    /**
     * Create response model for general proposals.
     *
     * @param proposal The proposal
     */
    public ProposalResponseModel(Proposal proposal) {
        if (proposal.getProposalType() == ProposalType.RULE) {
            proposal = (RuleProposal) proposal;
        }
        this.proposal = proposal.toString();
        this.type = proposal.getProposalType().name();
        this.accept = proposal.getAcceptVotes();
        this.reject = proposal.getRejectVotes();
        this.abstain = proposal.getAbstainVotes();
        this.creationDate = proposal.getDate().toString();
    }

    /**
     * Create response model for rule proposals.
     *
     * @param proposal The proposal
     * @param type The type
     * @param accept The amount of accept votes
     * @param reject The amount of reject votes
     * @param abstain The amount of abstain votes
     * @param date The date the proposal was created on
     * @param ruleId The rule ID
     */
    public ProposalResponseModel(String proposal, ProposalType type, int accept,
                                 int reject, int abstain, CreationDate date, int ruleId) {
        this.proposal = proposal;
        this.type = type.name();
        this.accept = accept;
        this.reject = reject;
        this.abstain = abstain;
        this.creationDate = date.toString();
        this.ruleId = ruleId;
    }
}
