package nl.tudelft.sem.hoa.domain.hoa;

public class HoaAlreadyExistException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    public HoaAlreadyExistException(HoaId hoaId) {
        super(hoaId.toString());
    }
}
