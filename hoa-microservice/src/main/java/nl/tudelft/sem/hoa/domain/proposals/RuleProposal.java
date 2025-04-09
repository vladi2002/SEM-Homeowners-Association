package nl.tudelft.sem.hoa.domain.proposals;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.hoa.domain.CreationDate;

@Entity
@Table(name = "proposals")
@DiscriminatorValue("RULE")
@NoArgsConstructor
public class RuleProposal extends Proposal {

    @Column(name = "rule_id")
    private int ruleId;

    /**
     * Constructor for rule proposal with votes.
     *
     * @param proposalPk The proposal ID
     * @param proposal The proposal
     * @param proposalType The proposal type
     * @param acceptVotes The number of accept votes
     * @param rejectVotes The number of reject votes
     * @param abstainVotes The number of abstain votes
     * @param ruleId The rule ID
     */
    public RuleProposal(ProposalPk proposalPk, String proposal, ProposalType proposalType,
                        int acceptVotes, int rejectVotes, int abstainVotes, List<String> hasVoted, int ruleId) {
        super(proposalPk, proposal, proposalType, acceptVotes, rejectVotes, abstainVotes, hasVoted);
        this.ruleId = ruleId;
    }

    /**
     * Constructor for rule proposal with votes and date.
     *
     * @param proposalPk The proposal ID
     * @param proposal The proposal
     * @param proposalType The proposal type
     * @param date The date the rule proposal was created on
     * @param acceptVotes The number of accept votes
     * @param rejectVotes The number of reject votes
     * @param abstainVotes The number of abstain votes
     * @param ruleId The rule ID
     */
    public RuleProposal(ProposalPk proposalPk, String proposal, ProposalType proposalType,
                        CreationDate date, int acceptVotes, int rejectVotes, int abstainVotes,
                        List<String> hasVoted, int ruleId) {
        super(proposalPk, proposal, proposalType, date, acceptVotes, rejectVotes, abstainVotes, hasVoted);
        this.ruleId = ruleId;
    }

    /**
     * Constructor for rule proposal.
     *
     * @param proposalPk The proposal ID
     * @param proposalType The proposal type
     * @param proposal The proposal
     * @param ruleId The rule ID
     */
    public RuleProposal(ProposalPk proposalPk, String proposal, ProposalType proposalType,
                        List<String> hasVoted, int ruleId) {
        super(proposalPk, proposal, proposalType, hasVoted);
        this.ruleId = ruleId;
    }

    /**
     * Constructor for rule proposal with date.
     *
     * @param proposalPk The proposal ID
     * @param proposalType The proposal type
     * @param proposal The proposal
     * @param date The date the rule proposal was created on
     * @param ruleId The rule ID
     */
    public RuleProposal(ProposalPk proposalPk, String proposal, ProposalType proposalType,
                        CreationDate date, List<String> hasVoted, int ruleId) {
        super(proposalPk, proposal, proposalType, date, hasVoted);
        this.ruleId = ruleId;
    }

    public int getRuleId() {
        return ruleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RuleProposal)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        RuleProposal that = (RuleProposal) o;
        return ruleId == that.getRuleId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRuleId());
    }
}
