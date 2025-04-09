package nl.tudelft.sem.hoa.domain.hoa;

public class HoaDoesNotExistException extends  Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public HoaDoesNotExistException(String hoaId) {
        super(hoaId);
    }
}
