package nl.tudelft.sem.template.authentication.models;

import lombok.Data;

/**
 * Model representing an address update request.
 */
@Data
public class AddressUpdateRequestModel {
    private String netId;
    private String country;
    private String city;
    private String street;
    private int houseNumber;
    private String postalCode;
}
