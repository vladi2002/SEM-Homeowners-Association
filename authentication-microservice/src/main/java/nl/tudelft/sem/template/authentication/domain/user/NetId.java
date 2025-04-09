package nl.tudelft.sem.template.authentication.domain.user;

import lombok.EqualsAndHashCode;

/**
 * A DDD value object representing a NetID in our domain.
 */
@EqualsAndHashCode
public class NetId {
    private final transient String netIdValue;


    /**
     * Constructor for NetID object, that also validates the input.
     *
     * @param netId The NetID string that needs to be validated and added to the object
     * @throws UsernameNotAllowedException if input is invalid
     */
    public NetId(String netId) throws UsernameNotAllowedException {
        if (netId.matches("^[a-zA-Z0-9]+$") && netId.length() >= 4) {
            this.netIdValue = netId;
        } else {
            throw new UsernameNotAllowedException(netId);
        }
    }

    @Override
    public String toString() {
        return netIdValue;
    }
}
