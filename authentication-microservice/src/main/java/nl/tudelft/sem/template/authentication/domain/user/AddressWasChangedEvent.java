package nl.tudelft.sem.template.authentication.domain.user;

/**
 * A DDD domain event indicating a users address had changed.
 */
public class AddressWasChangedEvent {

    private final AppUser user;
    private final AddressSubdivision addressSubdivision;

    /**
     * Create new event for an address was changed.
     *
     * @param user The user whose address was changed
     * @param addressSubdivision The address subdivision that was changed
     */
    public AddressWasChangedEvent(AppUser user, AddressSubdivision addressSubdivision) {
        this.user = user;
        this.addressSubdivision = addressSubdivision;
    }

    public AppUser getUser() {
        return this.user;
    }

    public AddressSubdivision getAddressSubdivision() {
        return addressSubdivision;
    }
}
