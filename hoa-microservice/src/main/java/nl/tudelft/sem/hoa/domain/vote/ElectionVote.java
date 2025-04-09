package nl.tudelft.sem.hoa.domain.vote;

import java.lang.reflect.Member;

public class ElectionVote extends SpecialVote {

    private transient String electionId;
    private transient String applicantId;

    /** An ElectionVote is a vote for the board elections of one specific HOA.
     *
     * @param vote The vote component that the decorator pattern uses.
     * @param electionId ID to determine the election
     * @param applicantId ID of the applicant which this vote is for
     */
    public ElectionVote(Vote vote, String electionId, String applicantId) {
        super(vote);
        this.electionId = electionId;
        this.applicantId = applicantId;
    }

    public String getElectionId() {
        return electionId;
    }

    public String getApplicantId() {
        return applicantId;
    }
}
