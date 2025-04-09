package nl.tudelft.sem.activity.domain;

public class UserDoesNotBelongToThisHoaException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public UserDoesNotBelongToThisHoaException(HoaId hoa) {
        super("Not a member of " + hoa.toString() + ". You can't respond to this activity.");
    }
}
