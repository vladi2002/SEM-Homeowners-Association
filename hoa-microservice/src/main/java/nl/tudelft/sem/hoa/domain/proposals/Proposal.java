package nl.tudelft.sem.hoa.domain.proposals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.hoa.domain.CreationDate;
import nl.tudelft.sem.hoa.domain.DateAttributeConverter;
import nl.tudelft.sem.hoa.domain.HasEvents;
import nl.tudelft.sem.hoa.domain.vote.ProposalVote;



/**
 *  A DDD object representing a proposal in our domain.
 */
@Entity
@Table(name = "proposals")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "proposal_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("GENERAL")
@NoArgsConstructor
public class Proposal extends HasEvents {

    @EmbeddedId
    protected ProposalPk proposalPk;

    @Column(name = "proposal_string", nullable = false)
    protected String proposalString;

    @Column(name = "proposal_type", insertable = false, updatable = false)
    @Convert(converter = ProposalTypeAttributeConverter.class)
    protected ProposalType proposalType;

    @Column(name = "accept_votes")
    protected int acceptVotes;

    @Column(name = "reject_votes")
    protected int rejectVotes;

    @Column(name = "abstain_votes")
    protected int abstainVotes;

    @Column(name = "creation_date")
    @Convert(converter = DateAttributeConverter.class)
    protected CreationDate date;

    @Column(name = "has_voted")
    @Convert(converter = HasVotedAttributeConverter.class)
    protected List<String> hasVotedOnProposal;

    /**
     * Constructor for proposal with votes.
     *
     * @param proposalPk The proposal ID
     * @param proposalString The proposal
     * @param proposalType The proposal type
     * @param acceptVotes The number of accept votes
     * @param rejectVotes The number of reject votes
     * @param abstainVotes The number of abstain votes
     * @param hasVotedOnProposal List of user who have voted for this proposal
     */
    public Proposal(ProposalPk proposalPk, String proposalString, ProposalType proposalType,
                    int acceptVotes, int rejectVotes, int abstainVotes, List<String> hasVotedOnProposal) {
        this.proposalPk = proposalPk;
        this.proposalString = proposalString;
        this.proposalType = proposalType;
        this.acceptVotes = acceptVotes;
        this.rejectVotes = rejectVotes;
        this.abstainVotes = abstainVotes;
        this.date = new CreationDate(LocalDate.now());
        this.hasVotedOnProposal = new ArrayList<>(hasVotedOnProposal);
        this.recordThat(new ProposalWasCreatedEvent(proposalPk));
    }

    /**
     * Constructor for proposal with votes.
     *
     * @param proposalPk The proposal ID
     * @param proposalString The proposal
     * @param proposalType The proposal type
     * @param date The date the proposal was created on
     * @param acceptVotes The number of accept votes
     * @param rejectVotes The number of reject votes
     * @param abstainVotes The number of abstain votes
     * @param hasVotedOnProposal List of user who have voted for this proposal
     */
    public Proposal(ProposalPk proposalPk, String proposalString, ProposalType proposalType, CreationDate date,
                    int acceptVotes, int rejectVotes, int abstainVotes, List<String> hasVotedOnProposal) {
        this.proposalPk = proposalPk;
        this.proposalString = proposalString;
        this.proposalType = proposalType;
        this.acceptVotes = acceptVotes;
        this.rejectVotes = rejectVotes;
        this.abstainVotes = abstainVotes;
        this.date = date;
        this.hasVotedOnProposal = new ArrayList<>(hasVotedOnProposal);
        this.recordThat(new ProposalWasCreatedEvent(proposalPk));
    }

    /**
     * Constructor for proposal.
     *
     * @param proposalPk The proposal ID
     * @param proposalType The proposal type
     * @param proposalString The proposal
     * @param hasVotedOnProposal List of user who have voted for this proposal
     */
    public Proposal(ProposalPk proposalPk, String proposalString, ProposalType proposalType,
                    List<String> hasVotedOnProposal) {
        this.proposalPk = proposalPk;
        this.proposalString = proposalString;
        this.proposalType = proposalType;
        this.acceptVotes = 0;
        this.rejectVotes = 0;
        this.abstainVotes = 0;
        this.date = new CreationDate(LocalDate.now());
        this.hasVotedOnProposal = new ArrayList<>(hasVotedOnProposal);
        this.recordThat(new ProposalWasCreatedEvent(proposalPk));
    }

    /**
     * Constructor for proposal.
     *
     * @param proposalPk The proposal ID
     * @param proposalType The proposal type
     * @param proposalString The proposal
     * @param date The date the proposal was created on
     * @param hasVotedOnProposal List of user who have voted for this proposal
     */
    public Proposal(ProposalPk proposalPk, String proposalString, ProposalType proposalType,
                    CreationDate date, List<String> hasVotedOnProposal) {
        this.proposalPk = proposalPk;
        this.proposalString = proposalString;
        this.proposalType = proposalType;
        this.acceptVotes = 0;
        this.rejectVotes = 0;
        this.abstainVotes = 0;
        this.date = date;
        this.hasVotedOnProposal = new ArrayList<>(hasVotedOnProposal);
        this.recordThat(new ProposalWasCreatedEvent(proposalPk));
    }

    public ProposalType getProposalType() {
        return proposalType;
    }

    public String getProposalString() {
        return proposalString;
    }

    public int getAcceptVotes() {
        return acceptVotes;
    }

    public int getRejectVotes() {
        return rejectVotes;
    }

    public int getAbstainVotes() {
        return abstainVotes;
    }

    public CreationDate getDate() {
        return date;
    }

    /**
     * Get all votes in an array.
     *
     * @return An array containing the votes
     */
    public int[] getVotes() {
        int[] votes = {acceptVotes, rejectVotes, abstainVotes};
        return votes;
    }

    /**
     * Method to add single vote to a proposal.
     *
     * @param vote The vote that needs to be counted
     */
    public void updateVote(ProposalVote vote) {
        switch (vote.getDecision()) {
            case ACCEPT:
                acceptVotes++;
                break;
            case REJECT:
                rejectVotes++;
                break;
            case ABSTAIN:
                abstainVotes++;
                break;
            default:
                break;
        }
        hasVotedOnProposal.add(vote.getUserId());
        this.recordThat(new ProposalVotesWereUpdatedEvent(proposalPk, vote.getDecision()));
    }

    /**
     * Check if a user has voted on a proposal already.
     *
     * @param netId The netID of the user
     * @return True if user has voted on a proposal already
     */
    public boolean hasVoted(String netId) {
        for (String s : hasVotedOnProposal) {
            if (s.equals(netId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Proposal proposal1 = (Proposal) o;
        return Objects.equals(proposalPk, proposal1.proposalPk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proposalPk, proposalString, acceptVotes, rejectVotes, abstainVotes);
    }
}
