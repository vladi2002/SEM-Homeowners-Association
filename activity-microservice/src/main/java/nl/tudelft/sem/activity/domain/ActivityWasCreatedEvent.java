package nl.tudelft.sem.activity.domain;

import java.util.Date;

/**
 * A DDD domain event that indicated a user was created.
 */
public class ActivityWasCreatedEvent {
    private final int id;
    private final Organizer organizer;
    private Description eventDescription;
    private Date date;
    private final HoaId hoaId;

    /**
     * Constructor for event for creating activities when a user creates an activity.
     *
     * @param id activity id
     * @param org organizer username
     * @param desc activity description
     */
    public ActivityWasCreatedEvent(int id, Organizer org, Description desc, Date date, HoaId hoaId) {
        this.id = id;
        this.organizer = org;
        this.eventDescription = desc;
        this.date = date;
        this.hoaId = hoaId;
    }

    public int getId() {
        return this.id;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public Description getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(Description eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public HoaId getHoaId() {
        return hoaId;
    }
}
