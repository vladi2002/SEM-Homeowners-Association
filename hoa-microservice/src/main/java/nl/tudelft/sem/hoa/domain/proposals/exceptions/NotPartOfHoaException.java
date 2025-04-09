package nl.tudelft.sem.hoa.domain.proposals.exceptions;


import nl.tudelft.sem.hoa.domain.hoa.HoaId;

public class NotPartOfHoaException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    public NotPartOfHoaException(String netId, HoaId hoaId) {
        super(netId + " " + hoaId.toString());
    }
}
