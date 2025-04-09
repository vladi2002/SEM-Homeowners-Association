package nl.tudelft.sem.hoa.domain.hoa;


public class MemberAlreadyInHoaException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    public MemberAlreadyInHoaException(String user, HoaId hoaId) {
        super("user " + user + " is already in " + hoaId.toString());
    }

}
