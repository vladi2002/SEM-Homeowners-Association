package nl.tudelft.sem.hoa.domain.hoa;

import java.util.Objects;
import lombok.NoArgsConstructor;

@SuppressWarnings("PMD")
@NoArgsConstructor
public class Address {

    private String country;
    private String city;
    private String street;
    private int houseNumber;
    private String postalCode;

    /**
     * Constructor for address object.
     *
     * @param country The country of address
     * @param city The city of address
     * @param street The street of address
     * @param houseNumber The house number of address
     * @param postalCode The postal code of address
     */
    public Address(String country, String city, String street, int houseNumber, String postalCode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return houseNumber == address.houseNumber && Objects.equals(country, address.country)
            && Objects.equals(city, address.city) && Objects.equals(street, address.street)
            && Objects.equals(postalCode, address.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, street, houseNumber, postalCode);
    }

    @Override
    public String toString() {
        return "Address{ country='" + country
            + ", city='" + city
            + ", street='" + street
            + ", houseNumber=" + houseNumber
            + ", postalCode='" + postalCode + '}';
    }
}
