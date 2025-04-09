package nl.tudelft.sem.template.authentication.application.user;

import nl.tudelft.sem.template.authentication.domain.user.PasswordWasChangedEvent;
import nl.tudelft.sem.template.authentication.domain.user.UserWasCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This event listener is automatically called when a domain entity is saved
 * which has stored events of type: PasswordWasChanged.
 */
@Component
public class PasswordWasChangedListener {
    /**
     * The name of the function indicated which event is listened to.
     * The format is onEVENTNAME.
     *
     * @param event The event to react to
     */
    @EventListener
    public void onPasswordWasChanged(PasswordWasChangedEvent event) {
        // Handler code here
        System.out.println("Account with netID (" + event.getUser().getNetId().toString() + ") was changed.");
    }
}
