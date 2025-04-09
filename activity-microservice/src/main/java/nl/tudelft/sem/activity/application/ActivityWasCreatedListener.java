package nl.tudelft.sem.activity.application;

import nl.tudelft.sem.activity.domain.ActivityWasCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This event listener is automatically called when a domain entity is saved
 * which has stored events of type: UserWasCreated.
 */
@Component
public class ActivityWasCreatedListener {
    /**
     * The name of the function indicated which event is listened to.
     * The format is onEVENTNAME.
     *
     * @param event The event to react to
     */
    @EventListener
    public void onActivityWasCreated(ActivityWasCreatedEvent event) {
        // Handler code here
        System.out.println("Activity (ID: " + event.getId() + ", Organizer: " + event.getOrganizer().toString()
                + ", Description: " + event.getEventDescription().toString()
            + ", Date and Time: " + event.getDate().toString()
            + ", HOA: " + event.getHoaId().toString() + ") was created.");
    }
}
