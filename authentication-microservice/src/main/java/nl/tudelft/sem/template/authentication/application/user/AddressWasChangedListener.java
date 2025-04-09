package nl.tudelft.sem.template.authentication.application.user;

import nl.tudelft.sem.template.authentication.domain.user.AddressSubdivision;
import nl.tudelft.sem.template.authentication.domain.user.AddressWasChangedEvent;
import nl.tudelft.sem.template.authentication.domain.user.AppUser;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AddressWasChangedListener {

    /**
     * Invoked when an address is changed, with the user and information that was changed.
     *
     * @param event The event to react to
     */
    @EventListener
    public void onAddressWasChanged(AddressWasChangedEvent event) {
        AppUser user = event.getUser();
        AddressSubdivision subdivision = event.getAddressSubdivision();
        String part;
        String change;

        switch (subdivision) {
            case country:
                part = "Country";
                change = user.getCountry();
                break;
            case city:
                part = "City";
                change = user.getCity();
                break;
            case postalCode:
                part = "Postal code";
                change = user.getPostalCode();
                break;
            case houseNumber:
                part = "House number";
                change = String.valueOf(user.getHouseNumber());
                break;
            case street:
                part = "Street";
                change = user.getStreet();
                break;
            default:
                part = "{part is empty}";
                change = "{change is empty}";
                break;
        }
        System.out.println(part + " of user \"" + user.toString() + "\" was changed to \"" + change + "\".");
    }
}
