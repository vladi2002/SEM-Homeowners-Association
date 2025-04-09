package nl.tudelft.sem.activity.domain;

public class ActivitiesNotFoundException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public ActivitiesNotFoundException(HoaId hoa) {
        super("HOA: " + hoa.toString() + " does not correspond to any activity");
    }
}
