package nl.tudelft.sem.hoa.domain.hoa;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.hoa.domain.HasEvents;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "hoas")
@NoArgsConstructor
public class Hoa extends HasEvents {


    /*
     * A DDD entity representing a Hoa in our domain.
     */

    /**
     * Identifier for the Hoa.
     */
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    //@Column(name = "type", nullable = false, updatable = false)
    //private final String type = "HOA";

    @Column(name = "hoaID")
    @Convert(converter = HoaIdAttributeConverter.class)
    private HoaId hoaId;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;


    /**
     * Create new Hoa.
     *
     * @param hoaId   custom Name of Hoa
     * @param country Country of Hoa
     * @param city    City of Hoa
     */
    public Hoa(HoaId hoaId, String country, String city) {
        this.hoaId = hoaId;
        this.country = country;
        this.city = city;
        this.recordThat(new HoaWasCreatedEvent(hoaId));
    }

    @Override
    public String toString() {
        return "Hoa {"
                + id
                + ", hoaId= "
                + hoaId
                + ", country= "
                + country
                + ", city= "
                + city
                + '}';
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HoaId getHoaId() {
        return hoaId;
    }


    public String getCountry() {
        return country;
    }


    public String getCity() {
        return city;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hoa hoa = (Hoa) o;
        return id == hoa.id && Objects.equals(hoaId, hoa.hoaId)
                && Objects.equals(country, hoa.country)
                && Objects.equals(city, hoa.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hoaId, country, city);
    }
}
