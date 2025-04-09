package nl.tudelft.sem.activity.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

/**
 * A DDD entity representing an application user in our domain.
 */
@Entity
@Table(name = "activities")
@NoArgsConstructor
public class Activity extends HasEvents {
    /**
     * Identifier for the application user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column(name = "organizer_name", nullable = false)
    @Convert(converter = OrganizerAttributeConverter.class)
    private Organizer organizer;

    @Column(name = "description", nullable = false)
    @Convert(converter = DescriptionAttributeConverter.class)
    private Description description;

    @Column(name = "date", nullable = false)
    @Convert(converter = DateAttributeConverter.class)
    private Date date;

    @Column(name = "hoaId", nullable = false)
    @Convert(converter = HoaAttributeConverter.class)
    private HoaId hoaId;

    @Column(name = "responses", nullable = false)
    @Convert(converter = ResponsesAttributeConverter.class)
    private List<Response> responseList;

    /**
     * Create new application user.
     *
     * @param organizer   The Organizer for the new activity
     * @param description The description for the new activity
     * @param date The date and time for the new activity
     */
    public Activity(Organizer organizer, Description description, Date date, HoaId hoaId) {
        this.organizer = organizer;
        this.description = description;
        this.date = date;
        this.hoaId = hoaId;
        this.responseList = new ArrayList<>();
        this.recordThat(new ActivityWasCreatedEvent(this.id, organizer, description, date, hoaId));
    }

    /**
     * Sets the response of the organizer to GOING as the organizer should always be present.
     */
    public void setOrganizerResponseToGoing() {
        Builder builder = new ResponseBuilder();
        builder.setResponderName(organizer.toString());
        builder.setResponseOption(ResponseOption.GOING);
        Response responseActivity = builder.build();
        this.responseList.add(responseActivity);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //    public void changeDescription(Description description) {
    //        this.description = description;
    //        this.recordThat(new DescriptionWasChangedEvent(this.description));
    //    }

    //    public Organizer getOrganizer() {
    //        return organizer;
    //    }

    //    public Description getDescription() {
    //        return description;
    //    }

    public Date getDate() {
        return date;
    }

    public HoaId getHoaId() {
        return hoaId;
    }

    public List<Response> getResponseList() {
        return responseList;
    }

    //    public void setResponseList(List<Response> responseList) {
    //        this.responseList = responseList;
    //    }

    public void addResponse(Response response) {
        this.responseList.add(response);
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Activity appActivity = (Activity) o;
        return id == (appActivity.id);
    }

    @Override
    public String toString() {
        return "Activity: " + this.id + ", organized by: " + this.organizer.toString()
            + ", on " + this.date.toString() + " at " + this.hoaId.toString()
            + " with description: " + this.description.toString() + ". Responses: " + this.responseList.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizer, description, date, hoaId, responseList);
    }
}
