package nl.tudelft.sem.hoa.application;

import java.util.Locale;
import nl.tudelft.sem.hoa.domain.proposals.ProposalVotesWereUpdatedEvent;
import nl.tudelft.sem.hoa.domain.proposals.ProposalWasCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProposalListener {

    /**
     * Invoked when a proposal is created.
     *
     * @param event The event to react to
     */
    @EventListener
    public void onProposalWasCreatedEvent(ProposalWasCreatedEvent event) {
        System.out.println(event.getProposalId() + " was created.");
    }

    /**
     * Invoked when a proposals votes are updated.
     *
     * @param event The event to react to
     */
    @EventListener
    public void onProposalVotesWereUpdatedEvent(ProposalVotesWereUpdatedEvent event) {
        System.out.println(event.getProposalId() + " votes were updated: added "
            + event.getDecision().toString().toLowerCase(Locale.ENGLISH) + " vote.");
    }
}
