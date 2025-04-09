package nl.tudelft.sem.hoa.domain;

import nl.tudelft.sem.hoa.domain.hoa.HoaId;

public class HoaIdNotFoundException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    public HoaIdNotFoundException(HoaId hoaId) {
        super(hoaId.toString());
    }
}
