package nl.tudelft.sem.template.authentication.domain.user;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.transaction.Transactional;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.authentication.domain.HasEvents;
import org.hibernate.annotations.DynamicUpdate;

/**
 * A DDD entity representing an application user in our domain.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@DynamicUpdate

@SuppressWarnings("PMD")
public class AppUser extends HasEvents {
    /**
     * Identifier for the application user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private int id;

    @Column(name = "net_id", nullable = false, unique = true)
    @Convert(converter = NetIdAttributeConverter.class)
    private NetId netId;

    @Column(name = "password_hash", nullable = false)
    //@Convert(converter = HashedPasswordAttributeConverter.class)
    private String password;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "house_number")
    private int houseNumber;

    @Column(name = "postal_code")
    private String postalCode;

    /**
     * Create new application user.
     *
     * @param netId The NetId for the new user
     * @param password The password for the new user
     */
    public AppUser(NetId netId, HashedPassword password) {
        this.netId = netId;
        this.password = password.toString();
        this.country = null;
        this.city = null;
        this.street = null;
        this.houseNumber = 0;
        this.postalCode = null;
        this.recordThat(new UserWasCreatedEvent(netId));
    }

    /**
     * Create new application user with address.
     *
     * @param netId The NetId for the new user
     * @param password The password for the new user
     * @param country The country for the new user
     * @param city The city for the new user
     * @param street The street number for the new user
     * @param houseNumber The house number for the new user
     * @param postalCode The postal code for the new user
     */
    public AppUser(NetId netId, HashedPassword password, String country,
                   String city, String street, int houseNumber, String postalCode) {
        this.netId = netId;
        this.password = password.toString();
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.recordThat(new UserWasCreatedEvent(netId));
    }

    /**
     * Changes Password.
     *
     * @param password changed password
     */
    public void changePassword(HashedPassword password) {
        this.password = password.toString();
        System.out.println("password changed");
        this.recordThat(new PasswordWasChangedEvent(this));
    }

    public int getId() {
        return id;
    }

    public NetId getNetId() {
        return netId;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public Address getAddress() {
        return new Address(country, city, street, houseNumber, postalCode);
    }

    private void changeCountry(String country) {
        this.country = country;
        this.recordThat(new AddressWasChangedEvent(this, AddressSubdivision.country));
    }

    private void changeCity(String city) {
        this.city = city;
        this.recordThat(new AddressWasChangedEvent(this, AddressSubdivision.city));
    }

    private void changePostalCode(String postalCode) {
        this.postalCode = postalCode;
        this.recordThat(new AddressWasChangedEvent(this, AddressSubdivision.postalCode));
    }

    private void changeStreet(String street) {
        this.street = street;
        this.recordThat(new AddressWasChangedEvent(this, AddressSubdivision.street));
    }

    private void changeHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
        this.recordThat(new AddressWasChangedEvent(this, AddressSubdivision.houseNumber));
    }

    /**
     * Changes address using private methods, that are only called if needed.
     *
     * @param address The address that needs to be stored
     */
    public void changeAddress(Address address) {
        if (address.getCountry() != null) {
            this.changeCountry(address.getCountry());
        }
        if (address.getCity() != null) {
            this.changeCity(address.getCity());
        }
        if (address.getStreet() != null) {
            this.changeStreet(address.getStreet());
        }
        if (address.getHouseNumber() != 0) {
            this.changeHouseNumber(address.getHouseNumber());
        }
        if (address.getPostalCode() != null) {
            this.changePostalCode(address.getPostalCode());
        }
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
        AppUser appUser = (AppUser) o;
        return id == (appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(netId);
    }

    @Override
    public String toString() {
        return "AppUser{id=" + id + ", netId=" + netId + '}';
    }
}
